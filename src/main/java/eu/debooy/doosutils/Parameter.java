/*
 * Copyright (c) 2021 Marco de Booij
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

import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import org.json.simple.JSONObject;


/**
 * @author Marco de Booij
 */
public final class Parameter {
  public static final String  ERR_PAR_ATTRIBUUT = "error.param.attribuut";
  public static final String  ERR_PAR_DATE      = "error.param.date";

  public static final String  JSON_PAR_EXTENSIE   = "extensie";
  public static final String  JSON_PAR_FORMAT     = "format";
  public static final String  JSON_PAR_HELP       = "help";
  public static final String  JSON_PAR_KORT       = "kort";
  public static final String  JSON_PAR_LANG       = "lang";
  public static final String  JSON_PAR_PARAMETER  = "parameter";
  public static final String  JSON_PAR_PARAM      = "param";
  public static final String  JSON_PAR_STANDAARD  = "standaard";
  public static final String  JSON_PAR_TYPE       = "type";
  public static final String  JSON_PAR_VERPLICHT  = "verplicht";

  public static final String  TPY_BESTAND = "bestand";
  public static final String  TPY_BOOLEAN = "boolean";
  public static final String  TPY_CHARSET = "charset";
  public static final String  TPY_DATE    = "date";
  public static final String  TPY_DOUBLE  = "double";
  public static final String  TPY_LOCALE  = "locale";
  public static final String  TPY_LONG    = "long";
  public static final String  TPY_MAP     = "map";
  public static final String  TPY_STRING  = "string";

  protected static final  List<String>  paramI18N     =
      Arrays.asList(JSON_PAR_FORMAT, JSON_PAR_HELP, JSON_PAR_KORT,
                    JSON_PAR_LANG, JSON_PAR_STANDAARD);
  protected static final  List<String>  paramSleutel  =
      Arrays.asList(JSON_PAR_EXTENSIE, JSON_PAR_FORMAT, JSON_PAR_HELP,
                    JSON_PAR_KORT, JSON_PAR_LANG, JSON_PAR_PARAMETER,
                    JSON_PAR_STANDAARD, JSON_PAR_TYPE, JSON_PAR_VERPLICHT);
  protected static final  List<String>  typeIntern    =
      Arrays.asList(TPY_BESTAND, TPY_CHARSET, TPY_LOCALE, TPY_MAP);

  private static final  ResourceBundle  resourceBundle  =
      ResourceBundle.getBundle(ParameterBundle.PARAMBUNDLE,
                               Locale.getDefault());

  private String    extensie;
  private String    format;
  private String    help;
  private String    kort;
  private String    lang;
  private String    param;
  private Object    standaard;
  private String    type      = TPY_STRING;
  private boolean   verplicht = false;
  private Object    waarde;

  protected Parameter() {}

  public Parameter(JSONObject jParam) {
    if (jParam.containsKey(JSON_PAR_EXTENSIE)) {
      setExtensie(jParam.get(JSON_PAR_EXTENSIE).toString());
    }
    if (jParam.containsKey(JSON_PAR_PARAMETER)) {
      setParam(jParam.get(JSON_PAR_PARAMETER).toString());
    }
    if (jParam.containsKey(JSON_PAR_TYPE)) {
      setType(jParam.get(JSON_PAR_TYPE).toString());
    }
    if (jParam.containsKey(JSON_PAR_VERPLICHT)) {
      setVerplicht(Boolean.TRUE.equals(jParam.get(JSON_PAR_VERPLICHT)));
    }

    paramI18N.forEach(sleutel -> {
      if (jParam.containsKey(sleutel)) {
        set(sleutel, jParam.get(sleutel));
      }
    });

    if (!jParam.containsKey(JSON_PAR_KORT)
        && !jParam.containsKey(JSON_PAR_LANG)) {
      setLang(param);
    }

    if (!jParam.containsKey(JSON_PAR_STANDAARD)) {
      setStandaard();
    }
  }

  public String getExtensie() {
    return extensie;
  }

  public String getFormat() {
    return format;
  }

  public String getHelp() {
    return help;
  }

  public String getKort() {
    return kort;
  }

  public String getLang() {
    return lang;
  }

  public String getParameter() {
    return param;
  }

  public Object getStandaard() {
    return standaard;
  }

  public String getType() {
    return type;
  }

  public Object getWaarde() {
    return waarde;
  }

  public boolean isValid() {
    return valideer().isEmpty();
  }

  public boolean isVerplicht() {
    return verplicht;
  }

  public final void set(String attribuut, Object waarde) {
    switch (attribuut.toLowerCase()) {
      case JSON_PAR_EXTENSIE:
        setExtensie((String) waarde);
        break;
      case JSON_PAR_FORMAT:
        setFormat((String) waarde);
        break;
      case JSON_PAR_HELP:
        setHelp((String) waarde);
        break;
      case JSON_PAR_KORT:
        setKort((String) waarde);
        break;
      case JSON_PAR_LANG:
        setLang((String) waarde);
        break;
      case JSON_PAR_STANDAARD:
        setStandaard(waarde);
        break;
      default:
        break;
    }
  }

  public void setExtensie(String extensie) {
    if (extensie.startsWith(".")) {
      this.extensie   = extensie;
    } else {
      this.extensie   = "." + extensie;
    }
  }

  public void setFormat(String format) {
    this.format     = format;
  }

  public final void setHelp(String help) {
    this.help       = help;
  }

  public final void setKort(String kort) {
    this.kort       = kort;
  }

  public final void setLang(String lang) {
    this.lang       = lang;
  }

  private void setParam(String param) {
    this.param      = param;
  }

  public final void setStandaard(Object standaard) {
    if (TPY_DATE.equalsIgnoreCase(type)) {
      if (DoosUtils.isBlankOrNull(format)) {
        format  = DoosConstants.DATUM;
      }
      try {
        this.standaard  = Datum.toDate(standaard.toString(), format);
      } catch (ParseException ex) {
        this.standaard  = standaard;
      }
    } else {
      this.standaard  = standaard;
    }
    if (!typeIntern.contains(type)) {
      type  = this.standaard.getClass().getSimpleName();
    }

    waarde  = this.standaard;
  }

  private void setStandaard() {
    switch (type.toLowerCase()) {
      case TPY_BOOLEAN:
        standaard = Boolean.FALSE;
        waarde    = standaard;
        break;
      case TPY_CHARSET:
        standaard = Charset.defaultCharset().name();
        waarde    = standaard;
        break;
      case TPY_LOCALE:
        standaard = Locale.getDefault().getLanguage();
        waarde    = standaard;
        break;
      case TPY_MAP:
        standaard = ".";
        waarde    = standaard;
        break;
      default:
        break;
    }
  }

  private void setType(String type) {
    this.type       = type;
  }

  private void setVerplicht(boolean verplicht) {
    this.verplicht  = verplicht;
  }

  public final void setWaarde(Object waarde) {
    if (waarde instanceof String) {
      setWaarde(waarde.toString());
    } else {
      this.waarde   = waarde;
    }
  }

  public final void setWaarde(String waarde) {
    switch (type.toLowerCase()) {
      case TPY_BESTAND:
        if (DoosUtils.isNotBlankOrNull(extensie)
            && waarde.endsWith(extensie)) {
          this.waarde =
              waarde.substring(0, waarde.length() - extensie.length());
        } else {
          this.waarde = waarde;
        }
        break;
      case TPY_BOOLEAN:
        if (waarde.isBlank()) {
          this.waarde = !((Boolean) standaard);
        } else {
          this.waarde = Boolean.parseBoolean(waarde);
        }
        break;
      case TPY_DATE:
        try {
          this.waarde = Datum.toDate(waarde, format);
        } catch (ParseException e) {
          this.waarde = null;
        }
        break;
      case TPY_DOUBLE:
        this.waarde   = Double.valueOf(waarde);
        break;
      case TPY_LONG:
        this.waarde   = Long.valueOf(waarde);
        break;
      default:
        this.waarde   = waarde;
        break;
    }
  }

  @Override
  public String toString() {
    var sType  = (null == standaard ? "" : "<"
        + standaard.getClass().getSimpleName() + "> ");

    return param + ": ["
            + "kort: [" + DoosUtils.nullToValue(kort, DoosConstants.NULL)
              + "], "
            + "lang: [" + DoosUtils.nullToValue(lang, DoosConstants.NULL)
              + "], "
            + "standaard: [" + sType
                + (null == standaard ? DoosConstants.NULL
                                     : standaard.toString()) + "], "
            + "type: [" + DoosUtils.nullToValue(type, DoosConstants.NULL)
              + "], "
            + "format: [" + DoosUtils.nullToValue(format, DoosConstants.NULL)
              + "], "
            + "extensie: [" + DoosUtils.nullToValue(extensie,
                                                    DoosConstants.NULL) + "], "
            + "verplicht: [" + verplicht + "], "
            + "waarde: ["+ (null == waarde ? DoosConstants.NULL
                                           : waarde.toString()) + "], "
            + "help: [" + DoosUtils.nullToValue(help, DoosConstants.NULL) + "]";
  }

  public List<String> valideer() {
    List<String>  errors  = new ArrayList<>();

    if (DoosUtils.isNotBlankOrNull(extensie)
        && !TPY_BESTAND.equalsIgnoreCase(type)) {
      errors.add(
          MessageFormat.format(resourceBundle.getString(ERR_PAR_ATTRIBUUT),
                               JSON_PAR_EXTENSIE, param));
    }
    if (DoosUtils.isNotBlankOrNull(format)
        && !TPY_DATE.equalsIgnoreCase(type)) {
      errors.add(
          MessageFormat.format(resourceBundle.getString(ERR_PAR_ATTRIBUUT),
                               JSON_PAR_FORMAT, param));
    }

    if (null == standaard) {
      return errors;
    }
    if (TPY_DATE.equalsIgnoreCase(type)
        && !TPY_DATE.equalsIgnoreCase(standaard.getClass().getSimpleName())) {
      errors.add(MessageFormat.format(resourceBundle.getString(ERR_PAR_DATE),
                                      param));
    }

    return errors;
  }
}
