/**
 * Copyright (c) 2005 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.doosutils;

import java.util.Scanner;


/**
 * @author Marco de Booij
 */
public final class DoosUtils {
  private DoosUtils() {}

  public static void foutNaarScherm(String regel) {
    System.err.println(regel);
  }

  public static String getWachtwoord(String prompt) {
    String  password;
    var     console   = System.console();
    if (null == console) {
      try (Scanner invoer = new Scanner(System.in)) {
        System.out.print(prompt + " ");
        password  = invoer.next();
      }
    } else {
      password  = new String(console.readPassword(prompt + " "));
    }

    return password;
  }

  public static boolean isBlankOrNull(Object obj) {
    return obj == null || obj.toString().trim().equals("");
  }

  public static boolean isFalse(String waarde) {
    return DoosConstants.ONWAAR.equalsIgnoreCase(waarde);
  }

  public static boolean isNotBlankOrNull(Object obj) {
    return obj != null && !obj.toString().trim().equals("");
  }

  public static boolean isTrue(String waarde) {
    return DoosConstants.WAAR.equalsIgnoreCase(waarde);
  }

  public static void naarScherm() {
    naarScherm("");
  }

  public static void naarScherm(String regel) {
    System.out.println(regel);
  }

  public static void naarScherm(String pString, int maxLengte) {
    naarScherm("", pString, maxLengte);
  }

  /**
   * Schrijf regel(s) van maxLengte op het scherm.
   *
   * @param pBegin
   * @param pString
   * @param maxLengte
   */
  public static void naarScherm(String pBegin, String pString, int maxLengte) {
    var beginLengte   = pBegin.length();
    var splitsLengte  = maxLengte - beginLengte;
    var begin         = pBegin;
    var leeg          =
        (beginLengte == 0 ? "" : stringMetLengte(" ", beginLengte));
    var string        = pString;

    while (string.length() > splitsLengte) {
      var splits  = string.substring(1, splitsLengte).lastIndexOf(" ");
      DoosUtils.naarScherm(begin + string.substring(0, splits + 1));
      begin   = leeg;
      string  = string.substring(splits + 2);
    }

    DoosUtils.naarScherm(begin + string);
  }

  public static String nullToEmpty(String string) {
    if (null == string) {
      return "";
    }

    return string;
  }

  public static Double nullToValue(Double waarde, Double defaultWaarde) {
    if (null == waarde) {
      return defaultWaarde;
    }

    return waarde;
  }

  public static Integer nullToValue(Integer waarde, Integer defaultWaarde) {
    if (null == waarde) {
      return defaultWaarde;
    }

    return waarde;
  }

  public static Long nullToValue(Long waarde, Long defaultWaarde) {
    if (null == waarde) {
      return defaultWaarde;
    }

    return waarde;
  }

  public static String nullToValue(String waarde, String defaultWaarde) {
    if (null == waarde) {
      return defaultWaarde;
    }

    return waarde;
  }

  public static String stringMetLengte(String string, int lengte) {
    if (string.length() >= lengte) {
      return string.substring(0, lengte);
    }

    return String.format("%" + lengte +"s", string);
  }

  public static String stringMetLengte(String string, int lengte,
                                       String padding) {
    var sb  = new StringBuilder(string);
    while (sb.length() < lengte) {
      sb.append(padding);
    }
    sb.setLength(lengte);

    return sb.toString();
  }

  public static int telTeken(String string, char teken) {
    var aantal  = 0;
    for (int i = 0; i < string.length(); i++) {
      if (string.charAt(i) == teken) {
        aantal++;
      }
    }

    return aantal;
  }
}
