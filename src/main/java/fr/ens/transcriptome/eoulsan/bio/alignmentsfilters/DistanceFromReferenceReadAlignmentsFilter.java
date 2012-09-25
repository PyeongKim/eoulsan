/*
 *                  Eoulsan development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public License version 2.1 or
 * later and CeCILL-C. This should be distributed with the code.
 * If you do not have a copy, see:
 *
 *      http://www.gnu.org/licenses/lgpl-2.1.txt
 *      http://www.cecill.info/licences/Licence_CeCILL-C_V1-en.txt
 *
 * Copyright for this code is held jointly by the Genomic platform
 * of the Institut de Biologie de l'École Normale Supérieure and
 * the individual authors. These should be listed in @author doc
 * comments.
 *
 * For more information on the Eoulsan project and its aims,
 * or to join the Eoulsan Google group, visit the home page
 * at:
 *
 *      http://www.transcriptome.ens.fr/eoulsan
 *
 */

package fr.ens.transcriptome.eoulsan.bio.alignmentsfilters;

import java.util.ArrayList;
import java.util.List;

import fr.ens.transcriptome.eoulsan.EoulsanException;

import net.sf.samtools.SAMRecord;

/**
 * This alignments filter keep alignments of a read according to the distance of
 * the read from the reference sequence on the genome. This filter is useful to
 * detect SNP and indel.
 * @since 1.2
 * @author Claire Wallon
 */
public class DistanceFromReferenceReadAlignmentsFilter extends
    AbstractReadAlignmentsFilter {

  public static final String FILTER_NAME = "distancefromreference";
  private int distance = -1;

  @Override
  public String getName() {
    return FILTER_NAME;
  }

  @Override
  public String getDescription() {
    return "After this filter, only the alignments which the distance from "
        + "the reference is lower than the given distance are keeped.";
  }

  @Override
  public void setParameter(final String key, final String value)
      throws EoulsanException {

    if (key == null || value == null)
      return;

    if ("threshold".equals(key.trim())) {

      try {
        this.distance = Integer.parseInt(value.trim());
      } catch (NumberFormatException e) {
        return;
      }

      if (this.distance < 0)
        throw new EoulsanException("Invalid distance: " + this.distance);
    } else

      throw new EoulsanException("Unknown parameter for "
          + getName() + " alignments filter: " + key);
  }

  @Override
  public void init() {

    if (this.distance < 0)
      throw new IllegalArgumentException(
          "The distance from the reference is not set for "
              + getName() + " alignments alignments filter.");
  }

  @Override
  public void filterReadAlignments(List<SAMRecord> records) {

    if (records == null)
      return;

    List<SAMRecord> recordsToKeep = new ArrayList<SAMRecord>();

    // single-end mode
    if (!records.get(0).getReadPairedFlag()) {
      for (SAMRecord r : records) {
        if (!r.getCigarString().contains("S")
            && !r.getCigarString().contains("H")
            && r.getAttribute("NM") != null
            && r.getIntegerAttribute("NM") <= this.distance) {
          recordsToKeep.add(r);
        }
      }
    }

    // paired-end mode
    else {
      for (int counterRecord = 0; counterRecord < records.size() - 1; counterRecord +=
          2) {
        final SAMRecord r1 = records.get(counterRecord);
        final SAMRecord r2 = records.get(counterRecord + 1);
        if (!r1.getCigarString().contains("S")
            && !r1.getCigarString().contains("H")
            && r1.getAttribute("NM") != null
            && r1.getIntegerAttribute("NM") <= this.distance
            && !r2.getCigarString().contains("S")
            && !r2.getCigarString().contains("H")
            && r2.getAttribute("NM") != null
            && r2.getIntegerAttribute("NM") <= this.distance) {
          recordsToKeep.add(r1);
          recordsToKeep.add(r2);
        }
      }
    }

    records.clear();
    records.addAll(recordsToKeep);

  }

}