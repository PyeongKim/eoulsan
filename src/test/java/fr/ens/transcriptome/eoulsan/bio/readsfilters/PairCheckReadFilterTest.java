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

package fr.ens.transcriptome.eoulsan.bio.readsfilters;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.ens.transcriptome.eoulsan.EoulsanException;
import fr.ens.transcriptome.eoulsan.bio.ReadSequence;

public class PairCheckReadFilterTest {

  @Test
  public void testAcceptReadSequence() throws EoulsanException {

    ReadFilter filter = new PairCheckReadFilter();

    try {
      filter.accept(null);
      assertTrue(true);
    } catch (NullPointerException e) {
      assertTrue(false);
    }

    ReadSequence read1 = new ReadSequence(0, "read/1", "ATG", "wxy");
    ReadSequence read2 = new ReadSequence(0, "read/2", "ATG", "wxy");

    assertTrue(filter.accept(read1, read2));

    try {
      assertFalse(filter.accept(null, read2));
      assertTrue(false);
    } catch (NullPointerException e) {
      assertTrue(true);
    }

    try {
      assertFalse(filter.accept(read1, null));
      assertTrue(false);
    } catch (NullPointerException e) {
      assertTrue(true);
    }
    
    try {
      assertFalse(filter.accept(null, null));
      assertTrue(false);
    } catch (NullPointerException e) {
      assertTrue(true);
    }
    
    
    


    read1.setName("read1");
    assertFalse(filter.accept(read1, read2));
    read1.setName("read/1");

    read1.setName("read/2");
    assertFalse(filter.accept(read1, read2));
    read1.setName("read/1");

    read1.setName("READ/1");
    assertFalse(filter.accept(read1, read2));
    read1.setName("read/1");

  }

}
