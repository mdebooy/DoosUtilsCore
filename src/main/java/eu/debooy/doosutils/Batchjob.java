/**
 * Copyright (c) 2020 Marco de Booij
 *
 * Licensed under the EUPL, Version 1.2 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.doosutils;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Marco de Booij
 */
public abstract class Batchjob {
  private static final  ResourceBundle  batchjobResource  =
      ResourceBundle.getBundle("BatchjobConstants", Locale.getDefault());

  protected static final  String  ERR_BEVATDIRECTORY  = "error.bevatdirectory";
  protected static final  String  ERR_INVALIDPARAMS   = "error.invalid.params";
  protected static final  String  ERR_NOBOOLEAN       = "error.no.boolean";
  protected static final  String  ERR_TOOLONBEKEND    = "error.tool.onbekend";

  protected static final  String  EXT_CSV   = ".csv";
  protected static final  String  EXT_JSON  = ".json";
  protected static final  String  EXT_PGN   = ".pgn";
  protected static final  String  EXT_TEX   = ".tex";
  protected static final  String  EXT_TRF   = ".trf";
  protected static final  String  EXT_ZIP   = ".zip";


  protected static final  String  HLP_CHARSETIN       = "help.charsetin";
  protected static final  String  HLP_CHARSETUIT      = "help.charsetuit";
  protected static final  String  HLP_INVOERDIR       = "help.invoerdir";
  protected static final  String  HLP_PARAMSVERPLICHT = "help.paramsverplicht";
  protected static final  String  HLP_PARAMVERPLICHT  = "help.paramverplicht";
  protected static final  String  HLP_UITVOERDIR      = "help.uitvoerdir";

  protected static final  String  LBL_FOUT  = "label.fout";
  protected static final  String  LBL_OPTIE = "label.optie";

  protected static final  String  MSG_KLAAR = "msg.klaar";

  protected static final  String  PAR_CHARSETIN   = "charsetin";
  protected static final  String  PAR_CHARSETUIT  = "charsetuit";
  protected static final  String  PAR_CSVBESTAND  = "csvbestand";
  protected static final  String  PAR_INVOERDIR   = "invoerdir";
  protected static final  String  PAR_JSONBESTAND = "jsonbestand";
  protected static final  String  PAR_READONLY    = "readonly";
  protected static final  String  PAR_TAAL        = "taal";
  protected static final  String  PAR_UITVOERDIR  = "uitvoerdir";

  protected static final  String  PFX_PARAMDASHES = "  --";

  protected static  Map<String, String> parameters  = new HashMap<>();

  public static void execute(String[] args) {
  }

  public static void help() {
    DoosUtils.naarScherm("help()");
  }

  protected static String getMelding(String code, Object... parameters) {
    if (null != parameters
        && parameters.length > 0) {
      return MessageFormat.format(batchjobResource.getString(code),
                                  parameters);
    }

    return batchjobResource.getString(code);
  }

  protected static String getParameter(String parameter) {
    if (parameters.containsKey(parameter)) {
      return parameters.get(parameter);
    }

    return "";
  }

  protected static String getParameterTekst(String parameter, int lengte) {
    return DoosUtils.stringMetLengte(PFX_PARAMDASHES + parameter,
                                     lengte + PFX_PARAMDASHES.length(), " ");
  }

  protected static boolean hasParameter(String parameter) {
    return parameters.containsKey(parameter);
  }

  protected static void printFouten(List<String> fouten) {
    if (!fouten.isEmpty() ) {
      fouten.forEach(DoosUtils::foutNaarScherm);
    }
  }

  public static void setBestandParameter(Arguments arguments,
                                         String parameter) {
    setBestandParameter(arguments, parameter, "");
  }

  public static void setBestandParameter(Arguments arguments,
                                         String parameter, String extentie) {
    if (arguments.hasArgument(parameter)) {
      String  bestand = arguments.getArgument(parameter);
      if (DoosUtils.isNotBlankOrNull(extentie)
          && bestand.endsWith(extentie)) {
        bestand = bestand.substring(0, bestand.length() - extentie.length());
      }
      parameters.put(parameter, bestand);
    }
  }

  public static void setDirParameter(Arguments arguments, String parameter) {
    setDirParameter(arguments, parameter, ".");
  }

  public static void setDirParameter(Arguments arguments, String parameter,
                                     String defaultDir) {
    String  directory;
    if (arguments.hasArgument(parameter)) {
      directory = arguments.getArgument(parameter);
    } else {
      directory = defaultDir;
    }
    if (!directory.endsWith(File.separator)) {
      directory += File.separator;
    }

    parameters.put(parameter, directory);
  }

  protected static void setParameter(Arguments arguments, String parameter) {
    if (arguments.hasArgument(parameter)) {
      parameters.put(parameter,
                     arguments.getArgument(parameter));
    }
  }

  protected static void setParameter(Arguments arguments,
                                     String parameter, String defaultwaarde) {
    if (arguments.hasArgument(parameter)) {
      parameters.put(parameter,
                     arguments.getArgument(parameter));
    } else {
      parameters.put(parameter, defaultwaarde);
    }
  }

  protected static void setParameter(String parameter, String waarde) {
    parameters.put(parameter, waarde);
  }
}
