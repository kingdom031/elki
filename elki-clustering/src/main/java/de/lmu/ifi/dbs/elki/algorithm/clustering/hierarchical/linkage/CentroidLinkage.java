/*
 * This file is part of ELKI:
 * Environment for Developing KDD-Applications Supported by Index-Structures
 *
 * Copyright (C) 2017
 * ELKI Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package de.lmu.ifi.dbs.elki.algorithm.clustering.hierarchical.linkage;

import de.lmu.ifi.dbs.elki.utilities.Alias;
import de.lmu.ifi.dbs.elki.utilities.documentation.Reference;
import de.lmu.ifi.dbs.elki.utilities.optionhandling.AbstractParameterizer;

/**
 * Centroid linkage clustering method, aka UPGMC: Unweighted Pair-Group Method
 * using Centroids.
 * 
 * Reference:
 * <p>
 * A. K. Jain and R. C. Dubes<br />
 * Algorithms for Clustering Data<br />
 * Prentice-Hall
 * </p>
 * 
 * @author Erich Schubert
 * @since 0.6.0
 */
@Reference(authors = "A. K. Jain and R. C. Dubes", //
    title = "Algorithms for Clustering Data", //
    booktitle = "Algorithms for Clustering Data, Prentice-Hall")
@Alias({ "centroid", "upgmc", "de.lmu.ifi.dbs.elki.algorithm.clustering.hierarchical.CentroidLinkageMethod" })
public class CentroidLinkage implements Linkage {
  /**
   * Static instance of class.
   */
  public static final CentroidLinkage STATIC = new CentroidLinkage();

  /**
   * Constructor.
   * 
   * @deprecated use the static instance {@link #STATIC} instead.
   */
  @Deprecated
  public CentroidLinkage() {
    super();
  }

  @Override
  public double combine(int sizex, double dx, int sizey, double dy, int sizej, double dxy) {
    final double f = 1. / (sizex + sizey);
    return (sizex * dx + sizey * dy - (sizex * sizey) * f * dxy) * f;
  }

  /**
   * Class parameterizer.
   * 
   * Returns the static instance.
   * 
   * @author Erich Schubert
   * 
   * @apiviz.exclude
   */
  public static class Parameterizer extends AbstractParameterizer {
    @Override
    protected CentroidLinkage makeInstance() {
      return STATIC;
    }
  }
} // Sokal and Michener (1958), Gower (1967)
