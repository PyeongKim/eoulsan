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

package fr.ens.transcriptome.eoulsan.data;

/**
 * This interface define a Datatype. It is used to check inputs and outputs of
 * steps.
 * @author Laurent Jourdren
 */
public interface DataType {

  /**
   * Get the name of the data type.
   * @return the name of the DataType
   */
  String getName();

  /**
   * The description of the datatype.
   * @return the the description of the DataType
   */
  String getDescription();

  /**
   * Get the prefix for the files of this DataType generated by Eoulsan.
   * @return the prefix for the files
   */
  String getPrefix();

  /**
   * Test if there is only one file for this DataType per analysis.
   * @return true if there is only one file for this DataType per analysis
   */
  boolean isOneFilePerAnalysis();

  /**
   * Test if the DataType is provided by the design file.
   * @return true if the DataType is provided by the design file
   */
  boolean isDataTypeFromDesignFile();

  /**
   * Get the name of the field of the design file that can provide the DataFile.
   * @return the design field name
   */
  String getDesignFieldName();
}
