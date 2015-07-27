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

package fr.ens.transcriptome.eoulsan.data.protocols;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import fr.ens.transcriptome.eoulsan.data.DataFile;
import fr.ens.transcriptome.eoulsan.data.DataFileMetadata;
import fr.ens.transcriptome.eoulsan.io.CompressionType;

/**
 * This abstract class define a storage protocol. It is useful to easily access
 * common resources like genomes or annotations.
 * @since 1.1
 * @author Laurent Jourdren
 */
public abstract class StorageDataProtocol extends AbstractDataProtocol {

  /**
   * Get the path where searching the files.
   * @return a string with the path where search the files
   */
  protected abstract String getBasePath();

  /**
   * Get the file extensions of the files to search.
   * @return a string with file extension of the files to search
   */
  protected String getExtension() {

    throw new NullPointerException(
        "No extension has been defined for the storage protocol: " + getName());
  }

  /**
   * Get the list of the file extensions of the files to search.
   * @return a list with file extensions of the files to search
   */
  protected List<String> getExtensions() {

    return Collections.singletonList(getExtension());
  }

  @Override
  public InputStream getData(final DataFile src) throws IOException {

    return getUnderLyingData(src).rawOpen();
  }

  @Override
  public OutputStream putData(final DataFile dest) throws IOException {

    throw new IOException(
        "PutData() method is no supported by " + getName() + " protocol");
  }

  @Override
  public boolean exists(final DataFile src, final boolean followLink) {

    try {

      return getUnderLyingData(src).exists();
    } catch (IOException e) {

      return false;
    }
  }

  @Override
  public DataFileMetadata getMetadata(final DataFile src) throws IOException {

    return getUnderLyingData(src).getMetaData();
  }

  @Override
  public boolean canRead() {

    return true;
  }

  @Override
  public boolean canWrite() {

    return false;
  }

  @Override
  public File getSourceAsFile(final DataFile src) {

    try {
      return getUnderLyingData(src).toFile();
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * Get the underlying Data.
   * @param src source to use
   * @return a the underlying DataFile
   * @throws IOException if an error occurs while getting the underlying
   *           DataFile
   */
  public DataFile getUnderLyingData(final DataFile src) throws IOException {

    final String basePath = getBasePath();

    if (basePath == null) {
      throw new IOException(getName() + " storage is not configured");
    }

    final DataFile baseDir = new DataFile(basePath);

    if (!baseDir.exists()) {
      throw new IOException(
          getName() + " storage base path does not exists: " + baseDir);
    }

    for (String extension : getExtensions()) {

      if (extension == null) {
        throw new NullPointerException(
            "One of the extensions of the storage protocol is null: "
                + getName());
      }

      final String filename = src.getName().toLowerCase().trim() + extension;

      for (CompressionType c : CompressionType.values()) {

        final DataFile f = new DataFile(baseDir, filename + c.getExtension());

        if (f.exists()) {
          return f;
        }
      }
    }

    throw new IOException("No " + getName() + " found for: " + src.getName());
  }

}
