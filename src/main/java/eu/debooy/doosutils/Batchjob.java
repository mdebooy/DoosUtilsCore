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

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Marco de Booij
 */
public abstract class Batchjob {
  private static final  ResourceBundle  batchjobResource  =
      ResourceBundle.getBundle("BatchjobConstants", Locale.getDefault());

  protected static  ParameterBundle paramBundle;

  protected static final  String  ERR_BEVATDIRECTORY  = "error.bevatdirectory";
  protected static final  String  ERR_INVALIDPARAMS   = "error.invalid.params";
  protected static final  String  ERR_NOBOOLEAN       = "error.no.boolean";
  protected static final  String  ERR_TOOLONBEKEND    = "error.tool.onbekend";

  protected static final  String  HLP_CHARSETIN       = "help.charsetin";
  protected static final  String  HLP_CHARSETUIT      = "help.charsetuit";
  protected static final  String  HLP_INVOERDIR       = "help.invoerdir";
  protected static final  String  HLP_PARAMSVERPLICHT = "help.paramsverplicht";
  protected static final  String  HLP_PARAMVERPLICHT  = "help.paramverplicht";
  protected static final  String  HLP_TEXBESTAND      = "help.texbestand";
  protected static final  String  HLP_UITVOERDIR      = "help.uitvoerdir";

  protected static final  String  LBL_FOUT        = "label.fout";
  protected static final  String  LBL_OPTIE       = "label.optie";
  protected static final  String  LBL_PARAM       = "label.param";
  protected static final  String  LBL_TEXBESTAND  = "label.texbestand";

  protected static final  String  MSG_KLAAR = "msg.klaar";

  protected static final  String  PAR_CHARSETIN   = "charsetin";
  protected static final  String  PAR_CHARSETUIT  = "charsetuit";
  protected static final  String  PAR_CSVBESTAND  = "csvbestand";
  protected static final  String  PAR_INVOERDIR   = "invoerdir";
  protected static final  String  PAR_JSONBESTAND = "jsonbestand";
  protected static final  String  PAR_READONLY    = "readonly";
  protected static final  String  PAR_TAAL        = "taal";
  protected static final  String  PAR_TEXBESTAND  = "texbestand";
  protected static final  String  PAR_UITVOERDIR  = "uitvoerdir";

  public static void execute(String[] args) {}

  public static void help() {
    if (null == paramBundle) {
      DoosUtils.naarScherm("help()");
    } else {
      paramBundle.help();
    }
  }

  protected static String getMelding(String code, Object... parameters) {
    if (null != parameters
        && parameters.length > 0) {
      return MessageFormat.format(batchjobResource.getString(code), parameters);
    }

    return batchjobResource.getString(code);
  }

  protected static void printFouten() {
    if (null != paramBundle) {
      printFouten(paramBundle.getErrors());
    }
  }

  protected static void printFouten(List<String> fouten) {
    if (!fouten.isEmpty() ) {
      fouten.forEach(DoosUtils::foutNaarScherm);
    }
  }

  protected static void setParameterBundle(ParameterBundle parameterBundle) {
    paramBundle = parameterBundle;
  }
}
