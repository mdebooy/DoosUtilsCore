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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.beanutils.BeanComparator;


/**
 * @author Marco de Booij
 */
public final class DoosUtils {
  private static final  String[]  GET_METHODS_PREFIXES  = {"get", "is"};

  private DoosUtils() {}

  public static List<Method> findGetters(Method[] methodes) {
    Comparator<Method>  comparator  = new BeanComparator("name");
    List<Method>        getters     = new ArrayList<>();
    for (var method : methodes) {
      if (method.getParameterTypes().length == 0) {
        for (String prefix : GET_METHODS_PREFIXES) {
          if (method.getName().startsWith(prefix)) {
              getters.add(method);
            break;
          }
        }
      }
    }
    Collections.sort(getters, comparator);

    return getters;
  }

  public static void foutNaarScherm(String regel) {
    System.err.println(regel);
  }

  public static String getEol() {
    return System.lineSeparator();
  }

  public static String getFileSep() {
    return System.getProperty("file.separator");
  }

  public static String getInvoer(String prompt) {
    var     console   = System.console();
    if (null == console) {
      try (var invoer = new Scanner(System.in)) {
        System.out.print(prompt + " ");
        return invoer.nextLine();
      }
    }

    return console.readLine(prompt + " ");
  }

  public static String getWachtwoord(String prompt) {
    String  password;
    var     console   = System.console();
    if (null == console) {
      try (var invoer = new Scanner(System.in)) {
        System.out.print(prompt + " ");
        password  = invoer.nextLine();
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

  public static void naarScherm(String pBegin, String pString, int maxLengte) {
    var beginLengte   = pBegin.length();
    var splitsLengte  = maxLengte - beginLengte;
    var begin         = pBegin;
    var leeg          =
        (beginLengte == 0 ? "" : stringMetLengte(" ", beginLengte));
    var string        = pString;

    while (string.length() > splitsLengte) {
      var splits  = string.substring(1, splitsLengte).lastIndexOf(" ");
      naarScherm(begin + string.substring(0, splits + 1));
      begin   = leeg;
      string  = string.substring(splits + 2);
    }

    naarScherm(begin + string);
  }

  public static void naarScherm(int indent, String pString, int maxLengte) {
    if (pString.length() <= maxLengte) {
      naarScherm(pString);
      return;
    }

    var splits  = pString.substring(1, maxLengte).lastIndexOf(" ");
    DoosUtils.naarScherm(pString.substring(0, splits + 1));

    naarScherm(stringMetLengte("", indent), pString.substring(splits + 2),
               maxLengte);
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

    return String.format(new StringBuilder().append("%")
                                            .append(lengte)
                                            .append("s").toString(), string);
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
    for (var i = 0; i < string.length(); i++) {
      if (string.charAt(i) == teken) {
        aantal++;
      }
    }

    return aantal;
  }

  public static String stripBeginEnEind(String tekst, String fix) {
    return stripBeginEnEind(tekst, fix, fix);
  }

  public static String stripBeginEnEind(String tekst,
                                        String prefix, String suffix) {
    if (tekst.startsWith(prefix)
        && tekst.endsWith(suffix)) {
      return tekst.substring(prefix.length(), tekst.length()-suffix.length());
    }

    return tekst;
  }

  public static String uniekeCharacters(String tekst){
    var uniek = new StringBuilder();
    for (var i = 0; i < tekst.length(); i++){
      if (uniek.indexOf(String.valueOf(tekst.charAt(i))) < 0){
        uniek.append(tekst.charAt(i));
      }
    }

    return uniek.toString();
  }
}
