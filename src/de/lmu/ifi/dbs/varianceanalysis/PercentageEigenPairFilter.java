package de.lmu.ifi.dbs.varianceanalysis;

import de.lmu.ifi.dbs.math.linearalgebra.EigenPair;
import de.lmu.ifi.dbs.math.linearalgebra.SortedEigenPairs;
import de.lmu.ifi.dbs.utilities.optionhandling.AttributeSettings;
import de.lmu.ifi.dbs.utilities.optionhandling.OptionHandler;
import de.lmu.ifi.dbs.utilities.optionhandling.ParameterException;
import de.lmu.ifi.dbs.utilities.optionhandling.WrongParameterValueException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The PercentageEigenPairFilter sorts the eigenpairs in decending order
 * of their eigenvalues and marks the first eigenpairs, whose sum of
 * eigenvalues is higher than the given percentage of the sum of all eigenvalues
 * as strong eigenpairs.
 *
 * @author Elke Achtert (<a href="mailto:achtert@dbs.ifi.lmu.de">achtert@dbs.ifi.lmu.de</a>)
 */

public class PercentageEigenPairFilter extends AbstractEigenPairFilter {
  /**
   * Holds the class specific debug status.
   */
  @SuppressWarnings({"UNUSED_SYMBOL"})
//  private static final boolean DEBUG = LoggingConfiguration.DEBUG;
  private static final boolean DEBUG = true;

  /**
   * The logger of this class.
   */
  @SuppressWarnings({"UNUSED_SYMBOL"})
  private Logger logger = Logger.getLogger(this.getClass().getName());

  /**
   * The default value for alpha.
   */
  public static final double DEFAULT_ALPHA = 0.85;

  /**
   * Option string for parameter alpha.
   */
  public static final String ALPHA_P = "alpha";

  /**
   * Description for parameter alpha.
   */
  public static final String ALPHA_D = "<double>a double between 0 and 1 specifying " +
                                       "the threshold for strong eigenvectors: " +
                                       "the strong eigenvectors explain a " +
                                       "portion of at least alpha of the total variance " +
                                       "(default is alpha = " + DEFAULT_ALPHA + ")";

  /**
   * The threshold for strong eigenvectors: the strong eigenvectors explain a
   * portion of at least alpha of the total variance.
   */
  private double alpha;

  /**
   * Provides a new EigenPairFilter that sorts the eigenpairs in decending order
   * of their eigenvalues and marks the first eigenpairs, whose sum of
   * eigenvalues is higher than the given percentage of the sum of all eigenvalues
   * as string eigenpairs.
   */
  public PercentageEigenPairFilter() {
    parameterToDescription.put(ALPHA_P + OptionHandler.EXPECTS_VALUE, ALPHA_D);
    optionHandler = new OptionHandler(parameterToDescription, getClass().getName());
  }

  /**
   * @see EigenPairFilter#filter(de.lmu.ifi.dbs.math.linearalgebra.SortedEigenPairs)
   */
  public FilteredEigenPairs  filter(SortedEigenPairs eigenPairs) {
    StringBuffer msg = new StringBuffer();
    if (DEBUG) {
      msg.append("\nalpha = ").append(alpha);
      msg.append("\nsortedEigenPairs = ").append(eigenPairs);
    }

    // init strong and weak eigenpairs
    List<EigenPair> strongEigenPairs = new ArrayList<EigenPair>();
    List<EigenPair> weakEigenPairs = new ArrayList<EigenPair>();

    // determine sum of eigenvalues
    double totalSum = 0;
    for (int i = 0; i < eigenPairs.size(); i++) {
      EigenPair eigenPair = eigenPairs.getEigenPair(i);
      totalSum += eigenPair.getEigenvalue();
    }
    if (DEBUG) {
      msg.append("\ntotalSum = ").append(totalSum);
    }

    // determine strong and weak eigenpairs
    double currSum = 0;
    boolean found = false;
    for (int i = 0; i < eigenPairs.size(); i++) {
      EigenPair eigenPair = eigenPairs.getEigenPair(i);
      currSum += eigenPair.getEigenvalue();
      if (currSum / totalSum >= alpha) {
        if (!found) {
          found = true;
          strongEigenPairs.add(eigenPair);
        }
        else {
          weakEigenPairs.add(eigenPair);
        }
      }
      else {
        strongEigenPairs.add(eigenPair);
      }
    }
    if (DEBUG) {
      msg.append("\nstrong EigenPairs = ").append(strongEigenPairs);
      msg.append("\nweak EigenPairs = ").append(weakEigenPairs);
      logger.fine(msg.toString());
    }

    return new FilteredEigenPairs(weakEigenPairs, strongEigenPairs);
  }


  /**
   * @see de.lmu.ifi.dbs.utilities.optionhandling.Parameterizable#description()
   */
  public String description() {
    StringBuffer description = new StringBuffer();
    description.append(PercentageEigenPairFilter.class.getName());
    description.append(" sorts the eigenpairs in decending order " +
                       "of their eigenvalues and returns the first eigenpairs, whose sum of" +
                       "eigenvalues is higher than the given percentage of the sum of all eigenvalues.\n");
    description.append(optionHandler.usage("", false));
    return description.toString();
  }

  /**
   * @see de.lmu.ifi.dbs.utilities.optionhandling.Parameterizable#setParameters(String[])
   */
  public String[] setParameters(String[] args) throws ParameterException {
    String[] remainingParameters = super.setParameters(args);

    //alpha
    if (optionHandler.isSet(ALPHA_P)) {
      String alphaString = optionHandler.getOptionValue(ALPHA_P);
      try {
        alpha = Double.parseDouble(alphaString);
        if (alpha <= 0 || alpha >= 1)
          throw new WrongParameterValueException(ALPHA_P, alphaString, ALPHA_D);
      }
      catch (NumberFormatException e) {
        throw new WrongParameterValueException(ALPHA_P, alphaString, ALPHA_D, e);
      }
    }
    else {
      alpha = DEFAULT_ALPHA;
    }

    setParameters(args, remainingParameters);
    return remainingParameters;
  }

  /**
   * @see de.lmu.ifi.dbs.utilities.optionhandling.Parameterizable#getAttributeSettings()
   */
  public List<AttributeSettings> getAttributeSettings() {
    List<AttributeSettings> attributeSettings = super.getAttributeSettings();

    AttributeSettings mySettings = attributeSettings.get(0);
    mySettings.addSetting(ALPHA_P, Double.toString(alpha));

    return attributeSettings;
  }


}
