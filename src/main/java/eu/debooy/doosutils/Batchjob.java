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

  public static final String  ERR_BEVATDIRECTORY  = "error.bevatdirectory";
  public static final String  ERR_INVALIDPARAMS   = "error.invalid.params";
  public static final String  ERR_NOBOOLEAN       = "error.no.boolean";
  public static final String  ERR_TOOLONBEKEND    = "error.tool.onbekend";

  public static final String  HLP_CHARSETIN       = "help.charsetin";
  public static final String  HLP_CHARSETUIT      = "help.charsetuit";
  public static final String  HLP_INVOERDIR       = "help.invoerdir";
  public static final String  HLP_PARAMSVERPLICHT = "help.paramsverplicht";
  public static final String  HLP_PARAMVERPLICHT  = "help.paramverplicht";
  public static final String  HLP_TEXBESTAND      = "help.texbestand";
  public static final String  HLP_UITVOERDIR      = "help.uitvoerdir";

  public static final String  LBL_FOUT        = "label.fout";
  public static final String  LBL_OPTIE       = "label.optie";
  public static final String  LBL_PARAM       = "label.param";
  public static final String  LBL_TEXBESTAND  = "label.texbestand";

  public static final String  MSG_KLAAR = "msg.klaar";

  public static final String  PAR_CHARSETIN   = "charsetin";
  public static final String  PAR_CHARSETUIT  = "charsetuit";
  public static final String  PAR_CSVBESTAND  = "csvbestand";
  public static final String  PAR_INVOERDIR   = "invoerdir";
  public static final String  PAR_JSONBESTAND = "jsonbestand";
  public static final String  PAR_READONLY    = "readonly";
  public static final String  PAR_TAAL        = "taal";
  public static final String  PAR_TEXBESTAND  = "texbestand";
  public static final String  PAR_UITVOERDIR  = "uitvoerdir";

  public static void execute(String[] args) {}

  protected static String getMelding(String code, Object... parameters) {
    if (null != parameters
        && parameters.length > 0) {
      return MessageFormat.format(batchjobResource.getString(code), parameters);
    }

    return batchjobResource.getString(code);
  }

  protected static void help() {
    if (null == paramBundle) {
      DoosUtils.naarScherm("help()");
    } else {
      paramBundle.help();
    }
  }

  protected static void klaar() {
    DoosUtils.naarScherm();
    DoosUtils.naarScherm(getMelding(MSG_KLAAR));
    DoosUtils.naarScherm();
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
