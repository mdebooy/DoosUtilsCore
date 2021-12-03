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

import static eu.debooy.doosutils.Batchjob.EXT_JSON;
import static eu.debooy.doosutils.DoosConstants.NA;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TreeMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * @author Marco de Booij
 */
public class ParameterBundle {
  public static final String  ERR_ARG_DUBBEL    = "error.arg.dubbel";
  public static final String  ERR_ARG_FOUTIEF   = "error.arg.foutief";
  public static final String  ERR_ARG_ONBEKEND  = "error.arg.onbekend";
  public static final String  ERR_CONF_AFWEZIG  = "error.config.param.afwezig";
  public static final String  ERR_CONFS_AFWEZIG = "error.config.params.afwezig";
  public static final String  ERR_CONF_BESTAND  = "error.config.afwezig";
  public static final String  ERR_CONF_FOUTIEF  = "error.config.foutief";
  public static final String  ERR_PAR_AFWEZIG   = "error.param.afwezig";
  public static final String  ERR_PARS_AFWEZIG  = "error.params.afwezig";
  public static final String  ERR_PAR_ONBEKEND  = "error.param.onbekend";
  public static final String  PARAMBUNDLE       = "ParameterBundle";

  public  static final  String  JSON_KEY_APPLICATIE = "applicatie";
  public  static final  String  JSON_KEY_BANNER     = "banner";
  private static final  String  JSON_KEY_BREEDTE    = "_breedte";
  public  static final  String  JSON_KEY_HELP       = "help";
  private static final  String  JSON_KEY_JAR        = "_jar";
  public  static final  String  JSON_KEY_PARAMETERS = "parameters";
  private static final  String  JSON_KEY_PREFIX     = "_prefixlengte";

  private static final  String  LBL_GEBRUIK     = "label.gebruik";
  private static final  String  LBL_PARAMETERS  = "label.parameters";

  protected static final  List<String>  paramVerplicht  =
      Arrays.asList(Parameter.JSON_PAR_PARAMETER);

  private static final  ResourceBundle  resourceBundle  =
      ResourceBundle.getBundle(PARAMBUNDLE, Locale.getDefault());

  private       String                  applicatie  = NA;
  private       String                  banner;
  private final String                  baseName;
  private       int                     breedte     = 80;
  private final ClassLoader             classloader;
  private final List<String>            errors      = new ArrayList<>();
  private       String                  help;
  private       String                  jar;
  private final Map<String, String>     kort        = new TreeMap<>();
  private final Map<String, String>     lang        = new TreeMap<>();
  private final Locale                  locale;
  private final Map<String, Parameter>  params      = new TreeMap<>();
  private       int                     prefix      = 20;

  private ParameterBundle(Builder builder) {
    baseName    = builder.getBaseName();
    classloader = builder.getClassloader();
    locale      = builder.getLocale();

    init();
  }

  public static final class Builder {
    private String      baseName    = PARAMBUNDLE;
    private ClassLoader classloader =
        ParameterBundle.class.getClassLoader();
    private Locale      locale      = Locale.getDefault();

    public ParameterBundle build() {
      return new ParameterBundle(this);
    }

    public ClassLoader getClassloader() {
      return classloader;
    }

    public String getBaseName() {
      return baseName;
    }

    public Locale getLocale() {
      return locale;
    }

    public Builder setClassloader(ClassLoader classloader) {
      this.classloader = classloader;
      return this;
    }

    public Builder setBaseName(String baseName) {
      this.baseName = baseName;
      return this;
    }

    public Builder setLocale(Locale locale) {
      this.locale = locale;
      return this;
    }
  }

  private void checkConfiguratie() {
    var afwezig = new ArrayList<String>();

    if (DoosUtils.isBlankOrNull(applicatie)
        || NA.equalsIgnoreCase(applicatie)) {
      afwezig.add(JSON_KEY_APPLICATIE);
    }
    if (DoosUtils.isBlankOrNull(banner)) {
      afwezig.add(JSON_KEY_BANNER);
    }
    if (DoosUtils.isBlankOrNull(jar)) {
      afwezig.add(JSON_KEY_JAR);
    }

    if (afwezig.isEmpty()) {
      return;
    }

    if (afwezig.size() == 1) {
      errors.add(
          MessageFormat.format(resourceBundle.getString(ERR_CONF_AFWEZIG),
                               afwezig.get(0)));
    } else {
      errors.add(
          MessageFormat.format(
              resourceBundle.getString(ERR_CONFS_AFWEZIG),
              String.join(", ", afwezig.subList(0, afwezig.size()-1)),
              afwezig.get(afwezig.size()-1)));
    }
  }

  private void  checkParam(Parameter parameter) {
    errors.addAll(parameter.valideer());
  }

  private void checkParams() {
    params.values().forEach(param -> checkParam(param));
  }

  public Object get(String parameter) {
    return getParameter(parameter);
  }

  public String getApplicatie() {
    return applicatie;
  }

  public String getBanner() {
    return banner;
  }

  public String getBaseName() {
    return baseName;
  }

  public Boolean getBoolean(String parameter) {
    return (Boolean) getParameter(parameter);
  }

  public Date getDate(String parameter) {
    return (Date) getParameter(parameter);
  }

  public Double getDouble(String parameter) {
    return (Double) getParameter(parameter);
  }

  public String[] getErrors() {
    return errors.toArray(new String[errors.size()]);
  }

  public String getHelp() {
    return help;
  }

  public Locale getLocale() {
    return locale;
  }

  public Long getLong(String parameter) {
    return (Long) getParameter(parameter);
  }

  public Object getParameter(String parameter) {
    if (params.containsKey(parameter)) {
      return params.get(parameter).getWaarde();
    }

    return null;
  }

  public Enumeration<String> getParameters() {
    return Collections.enumeration(params.keySet());
  }

  public String getString(String parameter) {
    return getParameter(parameter).toString();
  }

  public void help() {
    var lijn    = new StringBuilder();
    var pString = DoosUtils.stringMetLengte("", prefix);

    if (DoosUtils.isNotBlankOrNull(help)) {
      DoosUtils.naarScherm(help, 80);
      DoosUtils.naarScherm();
    }

    DoosUtils.naarScherm(resourceBundle.getString(LBL_GEBRUIK));
    lijn.append(" -jar ").append(jar).append(" ").append(applicatie);
    params.keySet()
          .stream()
          .map(sleutel -> params.get(sleutel))
          .forEachOrdered(param -> {
      var tekst = new StringBuilder();
      tekst.append(" -")
           .append(DoosUtils.nullToValue(param.getKort(),
                    DoosUtils.nullToEmpty(param.getLang())));
      if (!param.getType().equalsIgnoreCase(Parameter.TPY_BOOLEAN)) {
        tekst.append(" ")
             .append(DoosUtils.nullToValue(param.getLang(),
                      DoosUtils.nullToEmpty(param.getKort()))
                              .toUpperCase());
      }
      if (param.isVerplicht()) {
        lijn.append(" ").append(tekst.toString().trim());
      } else {
        lijn.append(" [").append(tekst.toString().trim()).append("]");
      }
    });
    DoosUtils.naarScherm(" java", lijn.toString(), breedte);
    DoosUtils.naarScherm();

    DoosUtils.naarScherm(resourceBundle.getString(LBL_PARAMETERS));
    params.keySet().forEach(sleutel -> {
      var     param = params.get(sleutel);
      String  pfix;
      var     sPfix = new StringBuilder();
      if (null != param.getKort()) {
        sPfix.append(" -").append(param.getKort());
      }
      if (null != param.getLang()) {
        sPfix.append(" --").append(param.getLang());
      }
      if (sPfix.length() < prefix) {
        pfix  = DoosUtils.stringMetLengte(sPfix.toString(), prefix, " ");
      } else {
        DoosUtils.naarScherm(sPfix.toString(), breedte);
        pfix  = pString;
      }
      DoosUtils.naarScherm(pfix, param.getHelp(), breedte);
    });
    DoosUtils.naarScherm();
  }

  private void init() throws MissingResourceException {
    // Standaard parameters
    JSONObject  json  = readJson(baseName + EXT_JSON);
    setConfiguratie(json);

    try {
      // In de juiste taal
      json  = readJson(baseName + "_" + locale.getLanguage() + EXT_JSON);
      setConfiguratie(json);
    } catch (MissingResourceException e) {
      // Mag gemist worden.
    }
    try {
      // In de juiste taal en land
      json  = readJson(baseName + "_" + locale.getLanguage() + "_"
                        + locale.getCountry() + EXT_JSON);
      setConfiguratie(json);
    } catch (MissingResourceException e) {
      // Mag gemist worden.
    }

    valideer();
  }

  public boolean isValid() {
    return errors.isEmpty();
  }

  public void printParameters() {
    params.values().forEach(param -> DoosUtils.naarScherm(param.toString()));
  }

  private JSONObject readJson(String bestand) {
    URL         bundle  = classloader.getResource(bestand);
    JSONObject  json;

    if (bundle == null) {
      throw new MissingResourceException(
          MessageFormat.format(resourceBundle.getString(ERR_CONF_BESTAND),
                               bestand),
          this.getClass().getName(), baseName);
    }

    try (BufferedReader  invoer  =
              new BufferedReader(new FileReader(bundle.getFile()))) {
      json    = (JSONObject) new JSONParser().parse(invoer);
    } catch (IOException | ParseException e) {
      throw new MissingResourceException(
          MessageFormat.format(resourceBundle.getString(ERR_CONF_BESTAND),
                               bestand),
          this.getClass().getName(), baseName);
    }

    return json;
  }

  private void setConfiguratie(JSONObject json) {
    if (json.containsKey(JSON_KEY_APPLICATIE)
        && NA.equals(applicatie)) {
      applicatie  = json.get(JSON_KEY_APPLICATIE).toString();
    }
    if (json.containsKey(JSON_KEY_BANNER)) {
      banner      = json.get(JSON_KEY_BANNER).toString();
    }
    if (json.containsKey(JSON_KEY_BREEDTE)) {
      breedte     = Integer.valueOf(json.get(JSON_KEY_BREEDTE).toString());
    }
    if (json.containsKey(JSON_KEY_HELP)) {
      help        = json.get(JSON_KEY_HELP).toString();
    }
    if (json.containsKey(JSON_KEY_JAR)) {
      jar         = json.get(JSON_KEY_JAR).toString();
    }
    if (json.containsKey(JSON_KEY_PARAMETERS)) {
      setParameters((JSONArray) json.get(JSON_KEY_PARAMETERS));
    }
    if (json.containsKey(JSON_KEY_PREFIX)) {
      prefix      = Integer.valueOf(json.get(JSON_KEY_PREFIX).toString());
    }
  }

  public void reset() {
    params.values().forEach(param -> param.setWaarde(param.getStandaard()));
  }

  public boolean setArg(String parameter, String waarde) {
    if (!params.containsKey(DoosUtils.nullToEmpty(parameter))) {
      return false;
    }

    Parameter param = params.get(parameter);

    if (waarde.isEmpty()
        && Parameter.TPY_BOOLEAN.equalsIgnoreCase(param.getType())) {
      param.setWaarde(Boolean.FALSE.equals(param.getStandaard()));
      return true;
    }

    param.setWaarde(waarde);

    var fouten  = errors.size();
    errors.addAll(param.valideer());

    return (fouten == errors.size());
  }

  public boolean setArgs(String[] args) {
    boolean correct = true;
    var     i       = 0;

    while (i < args.length) {
      if (!args[i].trim().startsWith("-")
          || args[i].trim().startsWith("---")) {
        errors.add(
            MessageFormat.format(resourceBundle.getString(ERR_ARG_FOUTIEF),
                                        args[i]));
        correct = false;
        i++;
        continue;
      }

      String  parameter;
      var     metVolgende = (i+1 < args.length
                              && !args[i+1].trim().startsWith("-"));
      String  waarde;
      if (args[i].trim().startsWith("--")) {
        if (args[i].contains("=")) {
          var split = args[i].substring(2).split("=");
          parameter = lang.get(split[0]);
          waarde    = split[1];
        } else {
          parameter = lang.get(args[i].substring(2));
          waarde    = metVolgende ? args[i+1] : "";
        }
      } else {
        parameter = kort.get(args[i].substring(1));
        waarde    = metVolgende ? args[i+1] : "";
      }

      if (!setArg(parameter, waarde)) {
        errors.add(
            MessageFormat.format(resourceBundle.getString(ERR_ARG_FOUTIEF),
                                        args[i]));
        correct = false;
      }

      if (metVolgende) {
        i++;
      }
      i++;
    }

    return correct;
  }

  private void setParamArg(String arg, String param,
                           Map<String, String> args) {
    if (null == arg) {
      return;
    }

    if (args.containsKey(arg)) {
      errors.add(MessageFormat.format(resourceBundle.getString(ERR_ARG_DUBBEL),
                                      arg));
      return;
    }

    args.put(arg, param);
  }

  private void setParamArgB(String arg, String param,
                            Map<String, String> args) {
    if (null == arg) {
      return;
    }

    if (!args.containsValue(param)) {
      errors.add(MessageFormat.format(
          resourceBundle.getString(ERR_PAR_ONBEKEND), param));
      return;
    }

    var huidig  = args.entrySet()
                      .stream()
                      .filter(p -> p.getValue().equals(param))
                      .map(Map.Entry::getKey)
                      .findFirst()
                      .orElse("");
    if (huidig.equals(arg)) {
      return;
    }
    if (args.containsKey(arg)) {
      errors.add(MessageFormat.format(resourceBundle.getString(ERR_ARG_DUBBEL),
                                      arg));
      return;
    }

    args.remove(huidig, param);
    args.put(arg, param);
  }

  private void setParameters(JSONArray parameters) {
    boolean basis = params.isEmpty();

    for (var parameter : parameters.toArray()) {
      var jParam  = (JSONObject) parameter;
      var sleutel = jParam.get(Parameter.JSON_PAR_PARAMETER).toString();
      if (basis) {
        var param = new Parameter(jParam);
        setParamArg(param.getKort(), param.getParameter(), kort);
        setParamArg(param.getLang(), param.getParameter(), lang);
        params.put(sleutel, param);
      } else {
        if (params.containsKey(sleutel)) {
          var param = params.get(sleutel);
          setParamArgB(param.getKort(), param.getParameter(), kort);
          setParamArgB(param.getLang(), param.getParameter(), lang);
          setParams(jParam, param);
        } else {
          errors.add(
              MessageFormat.format(resourceBundle.getString(ERR_PAR_ONBEKEND),
                               sleutel, locale.toString()));
        }
      }
    }
  }

  private void setParams(JSONObject parameters, Parameter param) {
    Parameter.paramI18N
             .stream()
             .filter(sleutel -> (parameters.containsKey(sleutel)))
             .forEachOrdered(sleutel ->
                     param.set(sleutel, parameters.get(sleutel).toString()));
  }

  @Override
  public String toString() {
    return "ParameterBundle: "
            + "Applicatie: [" + applicatie + "], "
            + "Banner: [" + banner + "], "
            + "BaseName: [" + baseName + "], "
            + "Help: [" + help + "], "
            + "Locale: [" + locale.toString() + "], "
            + "Parameters: [" + params.values().toString() + "]";
  }

  private void valideer() {
    checkConfiguratie();
    checkParams();
  }
}
