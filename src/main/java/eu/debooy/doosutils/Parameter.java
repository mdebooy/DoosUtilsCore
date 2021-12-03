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

import static eu.debooy.doosutils.DoosConstants.NULL;
import static eu.debooy.doosutils.ParameterBundle.PARAMBUNDLE;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 * @author Marco de Booij
 */
public class Parameter {
  public static final String  ERR_PAR_DATE    = "error.param.date";
  public static final String  ERR_PAR_FORMAT  = "error.param.format";

  public static final String  JSON_PAR_EXTRA      = "extra";
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
  public static final String  TPY_DATE    = "date";
  public static final String  TPY_DOUBLE  = "double";
  public static final String  TPY_LONG    = "long";
  public static final String  TPY_MAP     = "map";
  public static final String  TPY_STRING  = "string";

  protected static final  List<String>  paramI18N     =
      Arrays.asList(JSON_PAR_FORMAT, JSON_PAR_HELP, JSON_PAR_KORT,
                    JSON_PAR_LANG, JSON_PAR_STANDAARD);
  protected static final  List<String>  paramSleutel  =
      Arrays.asList(JSON_PAR_EXTRA, JSON_PAR_FORMAT, JSON_PAR_HELP,
                    JSON_PAR_KORT, JSON_PAR_LANG, JSON_PAR_PARAMETER,
                    JSON_PAR_STANDAARD, JSON_PAR_TYPE, JSON_PAR_VERPLICHT);
  protected static final  List<String>  typeIntern    =
      Arrays.asList(TPY_BESTAND, TPY_MAP);

  private static final  ResourceBundle  resourceBundle  =
      ResourceBundle.getBundle(PARAMBUNDLE, Locale.getDefault());

  private String[]  extra;
  private String    help;
  private String    format;
  private String    kort;
  private String    lang;
  private String    param;
  private Object    standaard;
  private String    type      = TPY_STRING;
  private boolean   verplicht = false;
  private Object    waarde;

  private Parameter() {}

  public Parameter(JSONObject jParam) {
    if (jParam.containsKey(JSON_PAR_EXTRA)) {
      setExtra((JSONArray) jParam.get(JSON_PAR_EXTRA));
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

    if (!jParam.containsKey(JSON_PAR_STANDAARD)) {
      setStandaard();
    }
  }

  public String[] getExtra() {
    return extra;
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

  private void setExtra(JSONArray params) {
    extra = new String[params.size()];
    for (var i = 0; i < params.size(); i++) {
      extra[i]  = params.get(i).toString();
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
    if (TPY_BOOLEAN.equalsIgnoreCase(type)) {
      standaard = Boolean.FALSE;
      waarde    = standaard;
    }
    if (TPY_MAP.equalsIgnoreCase(type)) {
      standaard = ".";
      waarde    = standaard;
    }
  }

  private void setType(String type) {
    this.type       = type;
  }

  private void setVerplicht(boolean verplicht) {
    this.verplicht  = verplicht;
  }

  public final void setWaarde(Object waarde) {
    this.waarde     = waarde;
  }

  public final void setWaarde(String waarde) {
    switch (type.toLowerCase()) {
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
        this.waarde     = Double.valueOf(waarde);
        break;
      case TPY_LONG:
        this.waarde     = Long.valueOf(waarde);
        break;
      default:
        this.waarde     = waarde;
        break;
    }
  }

  @Override
  public String toString() {
    var sType  = (null == standaard ? "" : "<"
        + standaard.getClass().getSimpleName() + "> ");

    return param + ": ["
            + "kort: [" + DoosUtils.nullToValue(kort, NULL) + "], "
            + "lang: [" + DoosUtils.nullToValue(lang, NULL) + "], "
            + "standaard: [" + sType
                + (null == standaard ? NULL : standaard.toString()) + "], "
            + "type: [" + DoosUtils.nullToValue(type, NULL) + "], "
            + "format: [" + DoosUtils.nullToEmpty(format) + "], "
            + "verplicht: [" + verplicht + "], "
            + "waarde: ["+ (null == waarde ? NULL : waarde.toString()) + "], "
            + "extra: [" + (null == extra ? NULL : extra) + "], "
            + "help: [" + DoosUtils.nullToValue(help, NULL) + "]";
  }

  public List<String> valideer() {
    List<String>  errors  = new ArrayList<>();

    if (DoosUtils.isNotBlankOrNull(format)
        && !TPY_DATE.equalsIgnoreCase(type)) {
      errors.add(MessageFormat.format(resourceBundle.getString(ERR_PAR_FORMAT),
                                      param));
    }
    if (TPY_DATE.equalsIgnoreCase(type)
        && !TPY_DATE.equalsIgnoreCase(standaard.getClass().getSimpleName())) {
      errors.add(MessageFormat.format(resourceBundle.getString(ERR_PAR_DATE),
                                      param));
    }

    return errors;
  }
}
