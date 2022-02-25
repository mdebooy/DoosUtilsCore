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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * @author Marco de Booij
 */
public class ParameterBundle {
  public static final String  ERR_CONF_AFWEZIG  = "error.config.param.afwezig";
  public static final String  ERR_CONFS_AFWEZIG = "error.config.params.afwezig";
  public static final String  ERR_CONF_BESTAND  = "error.config.afwezig";
  public static final String  ERR_CONF_DUBBEL   = "error.config.param.dubbel";
  public static final String  ERR_CONF_FOUTIEF  = "error.config.foutief";
  public static final String  ERR_CONF_ONBEKEND = "error.config.onbekend";
  public static final String  ERR_CONF_ONGELIJK = "error.config.types.ongelijk";
  public static final String  ERR_CONFS_DUBBEL   = "error.config.params.dubbel";
  public static final String  ERR_PAR_AFWEZIG   = "error.param.afwezig";
  public static final String  ERR_PAR_DUBBEL    = "error.param.dubbel";
  public static final String  ERR_PAR_FOUTIEF   = "error.param.foutief";
  public static final String  ERR_PAR_ONBEKEND  = "error.param.onbekend";
  public static final String  ERR_PARS_AFWEZIG  = "error.params.afwezig";
  public static final String  ERR_PARS_DUBBEL   = "error.params.dubbel";
  public static final String  ERR_PARS_FOUTIEF  = "error.params.foutief";
  public static final String  ERR_PARS_ONBEKEND = "error.params.onbekend";

  protected static final  String  EXT_JSON  = ".json";

  public static final String  PARAMBUNDLE       = "ParameterBundle";

  public    static final  String  JSON_KEY_APPLICATIE = "applicatie";
  public    static final  String  JSON_KEY_BANNER     = "banner";
  private   static final  String  JSON_KEY_BREEDTE    = "_breedte";
  public    static final  String  JSON_KEY_EXTRAHELP  = "extrahelp";
  public    static final  String  JSON_KEY_HELP       = "help";
  protected static final  String  JSON_KEY_JAR        = "_jar";
  public    static final  String  JSON_KEY_PARAMETERS = "parameters";
  private   static final  String  JSON_KEY_PREFIX     = "_prefixlengte";

  private static final  String  LBL_GEBRUIK     = "label.gebruik";
  private static final  String  LBL_PARAMETERS  = "label.parameters";

  protected static final  List<String>  paramVerplicht  =
      Arrays.asList(Parameter.JSON_PAR_PARAMETER);

  private static final  ResourceBundle  resourceBundle  =
      ResourceBundle.getBundle(PARAMBUNDLE, Locale.getDefault());

  private String  applicatie  = DoosConstants.NA;
  private String  banner;
  private int     breedte     = 80;
  private String  extrahelp;
  private String  help;
  private String  jar;
  private int     prefix      = 20;

  private final List<String>              argumenten  = new ArrayList<>();
  private final String                    baseName;
  private final ClassLoader               classloader;
  private final List<String>              dubbel      = new ArrayList<>();
  private final List<String>              errors      = new ArrayList<>();
  private final Map<String, String>       kort        = new TreeMap<>();
  private final Map<String, String>       lang        = new TreeMap<>();
  private final Locale                    locale;
  private final Map<String, Parameter>    params      = new TreeMap<>();
  private final IParameterBundleValidator validator;

  private ParameterBundle(Builder builder) {
    baseName    = builder.getBaseName();
    classloader = builder.getClassloader();
    locale      = builder.getLocale();
    validator   = builder.getValidator();

    init();
  }

  public static final class Builder {
    private String                    baseName    = PARAMBUNDLE;
    private ClassLoader               classloader =
        ParameterBundle.class.getClassLoader();
    private Locale                    locale      = Locale.getDefault();
    private IParameterBundleValidator validator;

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

    public IParameterBundleValidator getValidator() {
      return validator;
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

    public Builder setValidator(IParameterBundleValidator validator) {
      this.validator = validator;
      return this;
    }
  }

  private boolean checkArgs() {
    List<String>  afwezig = new ArrayList<>();
    params.values()
          .stream()
          .filter(Parameter::isVerplicht)
          .map(Parameter::getParameter)
          .forEach(sleutel -> {
      if (!argumenten.contains(sleutel)) {
        if (kort.containsValue(sleutel)) {
          afwezig.add("-" + getArgument(sleutel, kort));
        } else {
          afwezig.add("--" + getArgument(sleutel, lang));
        }
      }
     });
    setInEnkelDubbelVoud(afwezig, ERR_PAR_AFWEZIG, ERR_PARS_AFWEZIG);

    Set<String> dParam  = new HashSet<>();
    dubbel.forEach(sleutel -> {
      if (kort.containsValue(sleutel)) {
        dParam.add("-" + getArgument(sleutel, kort));
      } else {
        dParam.add("--" + getArgument(sleutel, lang));
      }
    });
    afwezig.clear();
    afwezig.addAll(dParam);
    setInEnkelDubbelVoud(afwezig, ERR_PAR_DUBBEL, ERR_PARS_DUBBEL);

    var fouten  = errors.size();
    if (null != validator) {
      errors.addAll(validator.valideer(params, argumenten));
    }

    return afwezig.isEmpty() && fouten == errors.size();
  }

  private void checkConfiguratie() {
    var afwezig = new ArrayList<String>();

    if (DoosUtils.isBlankOrNull(applicatie)
        || DoosConstants.NA.equalsIgnoreCase(applicatie)) {
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

    setInEnkelDubbelVoud(afwezig, ERR_CONF_AFWEZIG, ERR_CONFS_AFWEZIG);
  }

  private void  checkParam(Parameter parameter) {
    var standaard = parameter.getStandaard();

    if (null != standaard
        && standaard.toString().startsWith("_@")
        && standaard.toString().endsWith("@_")) {
      var parent  = getParent(standaard.toString());
      // Kan nu pas met 'zekerheid' gezet worden.
      if (params.containsKey(parent)) {
        if (!parameter.getType()
                      .equalsIgnoreCase(params.get(parent).getType())) {
          errors.add(
              MessageFormat.format(resourceBundle.getString(ERR_CONF_ONGELIJK),
                                   parameter.getParameter(), parent));
        }
      } else {
          errors.add(
              MessageFormat.format(resourceBundle.getString(ERR_CONF_ONBEKEND),
                                   parameter.getParameter(), parent));
      }
    }

    errors.addAll(parameter.valideer());
  }

  private void checkParams() {
    params.values().forEach(this::checkParam);
  }

  public boolean containsArgument(String parameter) {
    return argumenten.contains(parameter);
  }

  public boolean containsParameter(String parameter) {
    return DoosUtils.isNotBlankOrNull(get(parameter));
  }

  public void debug() {
    DoosUtils.naarScherm(Parameter.JSON_PAR_KORT + ": " + kort.toString());
    DoosUtils.naarScherm(Parameter.JSON_PAR_LANG + ": " + lang.toString());
    params.values().forEach(param -> DoosUtils.naarScherm(param.toString()));
    errors.forEach(DoosUtils::naarScherm);
  }

  public Object get(String parameter) {
    return getParameter(parameter);
  }

  public String getApplicatie() {
    return applicatie;
  }

  private String getArgument(String parameter,
                             Map<String, String> parameters) {
    return parameters.entrySet()
                     .stream()
                     .filter(p -> p.getValue().equals(parameter))
                     .map(Map.Entry::getKey)
                     .findFirst()
                     .orElse("");
  }

  public String getBanner() {
    return banner;
  }

  public String getBaseName() {
    return baseName;
  }

  public String getBestand(String parameter) {
    if (!params.containsKey(parameter)) {
      return null;
    }

    if (DoosUtils.isBlankOrNull(params.get(parameter).getExtensie())) {
      return getString(parameter);
    }

    return getBestand(parameter, params.get(parameter).getExtensie());
  }

  public String getBestand(String parameter, String extensie) {
    var bestand = DoosUtils.nullToValue(getString(parameter),
                                        Parameter.TPY_BESTAND);

    if (bestand.endsWith(extensie)) {
      return bestand;
    }

    return bestand + extensie;
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

  public List<String> getErrors() {
    return new ArrayList<>(errors);
  }

  public String getHelp() {
    return help;
  }

  private String getInvoer(String invoer) {
    if (invoer.contains(DoosUtils.getFileSep())) {
      return invoer;
    }

    if (argumenten.contains(Batchjob.PAR_INVOERDIR)) {
      return getString(Batchjob.PAR_INVOERDIR)
              + DoosUtils.getFileSep() + invoer;
    }

    return invoer;
  }

  public String getInvoerbestand(String bestand) {
    return getInvoer(getBestand(bestand));
  }

  public String getInvoerbestand(String bestand, String extensie) {
    return getInvoer(getBestand(bestand, extensie));
  }

  public Locale getLocale() {
    return locale;
  }

  public Long getLong(String parameter) {
    return (Long) getParameter(parameter);
  }

  public Object getParameter(String parameter) {
    if (!params.containsKey(parameter)) {
      return null;
    }

    var standaard = params.get(parameter).getStandaard();
    if (null != standaard
        && standaard.toString().startsWith("_@")
        && standaard.toString().endsWith("@_")
        && !argumenten.contains(parameter)) {
      return getParameter(getParent(standaard.toString()));
    }

    return params.get(parameter).getWaarde();
  }

  public Enumeration<String> getParameters() {
    return Collections.enumeration(params.keySet());
  }

  private String getParent(String standaard) {
    return standaard.substring(2, standaard.length() - 2);
  }

  public String getString(String parameter) {
    var param = getParameter(parameter);

    if (null == param) {
      return null;
    }

    return getParameter(parameter).toString();
  }

  private String getUitvoer(String uitvoer) {
    if (uitvoer.contains(DoosUtils.getFileSep())) {
      return uitvoer;
    }

    if (argumenten.contains(Batchjob.PAR_INVOERDIR)
        || argumenten.contains(Batchjob.PAR_UITVOERDIR)) {
      return getString(Batchjob.PAR_UITVOERDIR)
              + DoosUtils.getFileSep() + uitvoer;
    }

    return uitvoer;
  }

  public String getUitvoerbestand(String bestand) {
    return getUitvoer(getBestand(bestand));
  }

  public String getUitvoerbestand(String bestand, String extensie) {
    return getUitvoer(getBestand(bestand, extensie));
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
          .map(params::get)
          .forEachOrdered(param -> {
      var tekst = new StringBuilder();
      tekst.append(" -")
           .append(DoosUtils.nullToValue(param.getKort(),
                    "-" + DoosUtils.nullToEmpty(param.getLang())));
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
      DoosUtils.naarScherm(pfix,
                           MessageFormat.format(param.getHelp(),
                                                param.getStandaard()),
                           breedte);
    });
    DoosUtils.naarScherm();

    if (DoosUtils.isNotBlankOrNull(extrahelp)) {
      DoosUtils.naarScherm(extrahelp, 80);
      DoosUtils.naarScherm();
    }
  }

  private void init() throws MissingResourceException {
    // Standaard parameters
    var json  = readJson(baseName + EXT_JSON);
    setConfiguratie(json, Boolean.TRUE);

    try {
      // In de juiste taal
      json  = readJson(baseName + "_" + locale.getLanguage() + EXT_JSON);
      setConfiguratie(json, Boolean.FALSE);
    } catch (MissingResourceException e) {
      // Mag gemist worden.
    }
    try {
      // In de juiste taal en land
      json  = readJson(baseName + "_" + locale.getLanguage() + "_"
                        + locale.getCountry() + EXT_JSON);
      setConfiguratie(json, Boolean.FALSE);
    } catch (MissingResourceException e) {
      // Mag gemist worden.
    }

    valideer();
  }

  public boolean isValid() {
    return errors.isEmpty();
  }

  private JSONObject readJson(String bestand) {
    var         is    = classloader.getResourceAsStream(bestand);
    JSONObject  json;

    if (null == is) {
      throw new MissingResourceException(
          MessageFormat.format(resourceBundle.getString(ERR_CONF_BESTAND),
                               bestand),
          this.getClass().getName(), baseName);
    }
    try (var invoer  = new BufferedReader(new InputStreamReader(is))) {
      json    = (JSONObject) new JSONParser().parse(invoer);
    } catch (IOException | ParseException e) {
      throw new MissingResourceException(
          MessageFormat.format(resourceBundle.getString(ERR_CONF_BESTAND),
                               bestand),
          this.getClass().getName(), baseName);
    }

    return json;
  }

  private void setConfiguratie(JSONObject json, Boolean basis) {
    if (Boolean.TRUE.equals(basis) && json.containsKey(JSON_KEY_APPLICATIE)) {
      applicatie  = json.get(JSON_KEY_APPLICATIE).toString();
    }
    if (json.containsKey(JSON_KEY_BANNER)) {
      banner      = json.get(JSON_KEY_BANNER).toString();
    }
    if (json.containsKey(JSON_KEY_BREEDTE)) {
      breedte     = Integer.valueOf(json.get(JSON_KEY_BREEDTE).toString());
    }
    if (json.containsKey(JSON_KEY_EXTRAHELP)) {
      extrahelp   = json.get(JSON_KEY_EXTRAHELP).toString();
    }
    if (json.containsKey(JSON_KEY_HELP)) {
      help        = json.get(JSON_KEY_HELP).toString();
    }
    if (Boolean.TRUE.equals(basis) && json.containsKey(JSON_KEY_JAR)) {
      jar         = json.get(JSON_KEY_JAR).toString();
    }
    if (json.containsKey(JSON_KEY_PARAMETERS)) {
      setParameters((JSONArray) json.get(JSON_KEY_PARAMETERS), basis);
    }
    if (json.containsKey(JSON_KEY_PREFIX)) {
      prefix      = Integer.valueOf(json.get(JSON_KEY_PREFIX).toString());
    }
  }

  public void reset() {
    params.values().forEach(param -> param.setWaarde(param.getStandaard()));
  }

  private boolean setArg(String parameter, String waarde) {
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

  private boolean setArgLang(String arg, String arg2) {
    String  parameter;
    String  waarde;
    if (arg.contains("=")) {
      parameter = lang.get(arg.split("=")[0]);
      waarde    = stripQuotes(arg.substring(arg.indexOf("=")+1));
    } else {
      parameter = lang.get(arg);
      waarde    = arg2;
    }

    return setArg(parameter, waarde);
  }

  public boolean setArgs(String[] args) {
    var correct = true;
    var foutief = new ArrayList<String>();
    var i       = 0;

    dubbel.clear();
    while (i < args.length) {
      var arg = args[i];
      if (!args[i].trim().startsWith("-")
          || args[i].trim().startsWith("---")) {
        foutief.add(args[i]);
        correct = false;
        i++;
        continue;
      }

      var     parameter = args[i];
      var     waarde    = "";
      if (i+1 < args.length
          && !args[i+1].trim().startsWith("-")) {
        waarde  = stripQuotes(args[i+1]);
        i++;
      }

      if (parameter.trim().startsWith("--")) {
        parameter   = parameter.substring(2);
        correct     = setArgLang(parameter, waarde);
        if (parameter.contains("=")) {
          parameter = parameter.split("=")[0];
        }
        parameter   = lang.get(parameter);
      } else {
        parameter   = kort.get(parameter.substring(1));
        correct     = setArg(parameter, waarde);
      }

      if (!argumenten.contains(parameter)) {
        argumenten.add(parameter);
      } else {
        dubbel.add(parameter);
      }

      Parameter param;
      if (correct)  {
        param = params.get(parameter);
      } else {
        param = new Parameter();
        if (arg.startsWith("--")) {
          param.setLang(arg.substring(2).split("=")[0]);
        } else {
          param.setKort(arg.substring(1));
        }
      }

      if (!correct
          || DoosUtils.isBlankOrNull(param.getWaarde())) {
        foutief.add("-" + DoosUtils.nullToValue(
            param.getKort(),
            "-" + DoosUtils.nullToEmpty(param.getLang())));
      }

      i++;
    }

    setInEnkelDubbelVoud(foutief, ERR_PAR_FOUTIEF, ERR_PARS_FOUTIEF);

    return correct && checkArgs();
  }

  private void setInEnkelDubbelVoud(List<String> params,
                                    String enkelvoud, String dubbelvoud) {
    if (params.isEmpty()) {
      return;
    }

    if (params.size() == 1) {
      errors.add(
          MessageFormat.format(resourceBundle.getString(enkelvoud),
                               params.get(0)));
    }
    if (params.size() > 1) {
      errors.add(
          MessageFormat.format(
              resourceBundle.getString(dubbelvoud),
              String.join(", ", params.subList(0, params.size()-1)),
              params.get(params.size()-1)));
    }
  }

  private void setParamArg(String arg, String param,
                           Map<String, String> args, List<String> dubbel) {
    if (null == arg) {
      return;
    }

    if (args.containsKey(arg)) {
      dubbel.add(arg);
      return;
    }

    args.put(arg, param);
  }

  private void setParamArgB(String arg, String param,
                            Map<String, String> args,
                            List<String> dubbel, List<String> onbekend) {
    if (null == arg) {
      return;
    }

    if (!args.containsValue(param)) {
      onbekend.add(param);
      return;
    }

    var huidig  = getArgument(param, args);

    if (huidig.equals(arg)) {
      return;
    }
    if (args.containsKey(arg)) {
      dubbel.add(arg);
      return;
    }

    args.remove(huidig, param);
    args.put(arg, param);
  }

  private void setParameters(JSONArray parameters, Boolean basis) {
    var     onbekend  = new ArrayList<String>();

    dubbel.clear();
    for (var parameter : parameters.toArray()) {
      var jParam  = (JSONObject) parameter;
      var sleutel = jParam.get(Parameter.JSON_PAR_PARAMETER).toString();
      if (Boolean.TRUE.equals(basis)) {
        if (params.containsKey(sleutel)) {
          dubbel.add(sleutel);
        } else {
          var param = new Parameter(jParam);
          setParamArg(param.getKort(), param.getParameter(), kort, dubbel);
          setParamArg(param.getLang(), param.getParameter(), lang, dubbel);
          params.put(sleutel, param);
        }
      } else {
        if (params.containsKey(sleutel)) {
          var param = params.get(sleutel);
          setParamArgB((String) jParam.get(Parameter.JSON_PAR_KORT),
                       param.getParameter(), kort, dubbel, onbekend);
          setParamArgB((String) jParam.get(Parameter.JSON_PAR_LANG),
                       param.getParameter(), lang, dubbel, onbekend);
          setParams(jParam, param);
        } else {
          onbekend.add(sleutel);
        }
      }
    }

    setInEnkelDubbelVoud(onbekend, ERR_PAR_ONBEKEND, ERR_PARS_ONBEKEND);
    setInEnkelDubbelVoud(dubbel, ERR_CONF_DUBBEL, ERR_CONFS_DUBBEL);
  }

  private void setParams(JSONObject parameters, Parameter param) {
    Parameter.paramI18N
             .stream()
             .filter(parameters::containsKey)
             .forEachOrdered(sleutel ->
                     param.set(sleutel, parameters.get(sleutel).toString()));
  }

  private String stripQuotes(String string) {
    if (string.startsWith("\"")
        && string.endsWith("\"")
        && string.length() > 1) {
      return (string.substring(1, string.length() - 1));
    }

    return string;
  }

  @Override
  public String toString() {
    return "ParameterBundle: "
            + "Applicatie: [" + applicatie + "], "
            + "Banner: [" + banner + "], "
            + "BaseName: [" + baseName + "], "
            + "Extrahelp: [" + extrahelp + "], "
            + "Help: [" + help + "], "
            + "Locale: [" + locale.toString() + "], "
            + "Parameters: [" + params.values().toString() + "]";
  }

  private void valideer() {
    checkConfiguratie();
    checkParams();
  }
}
