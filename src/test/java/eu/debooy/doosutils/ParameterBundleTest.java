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

import static eu.debooy.doosutils.Parameter.TPY_BESTAND;
import static eu.debooy.doosutils.ParameterBundle.ERR_CONFS_DUBBEL;
import static eu.debooy.doosutils.ParameterBundle.ERR_CONF_AFWEZIG;
import static eu.debooy.doosutils.ParameterBundle.ERR_CONF_BESTAND;
import static eu.debooy.doosutils.ParameterBundle.ERR_CONF_DUBBEL;
import static eu.debooy.doosutils.ParameterBundle.ERR_PARS_AFWEZIG;
import static eu.debooy.doosutils.ParameterBundle.ERR_PARS_DUBBEL;
import static eu.debooy.doosutils.ParameterBundle.ERR_PAR_AFWEZIG;
import static eu.debooy.doosutils.ParameterBundle.ERR_PAR_DUBBEL;
import static eu.debooy.doosutils.ParameterBundle.ERR_PAR_ONBEKEND;
import static eu.debooy.doosutils.ParameterBundle.EXT_JSON;
import static eu.debooy.doosutils.ParameterBundle.JSON_KEY_BANNER;
import static eu.debooy.doosutils.ParameterBundle.JSON_KEY_HELP;
import static eu.debooy.doosutils.ParameterBundle.PARAMBUNDLE;
import eu.debooy.doosutils.test.BatchTest;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class ParameterBundleTest extends BatchTest {
  protected static final  ClassLoader CLASSLOADER =
      ParameterBundleTest.class.getClassLoader();
  protected static final  Locale      LOCALE      = new Locale("nl");

  private static final  String  APPLICATIE  = "applicatie";
  private static final  String  PARAMETERS  = "parameters";
  private static final  String  PARAMS      = "params";

  private static  Date  d2112;

  @BeforeClass
  public static void beforeClass() {
    resourceBundle   = ResourceBundle.getBundle(PARAMBUNDLE,
                                                Locale.getDefault());
    try {
      d2112           = Datum.toDate("2112/12/21", "yyyy/MM/dd");
    } catch (ParseException e) {
      // Onnodig.
    }
  }

  @Test
  public void testApplicatie1() {
    before();
    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(APPLICATIE)
                                     .setLocale(LOCALE)
                                     .build();
    after();

    assertTrue(parameterBundle.isValid());
  }

  @Test
  public void testApplicatie2() {
    String[]  args      = new String[] {"-a", "230", "-k", "2112/12/21",
                                        "-u", "/tmp", "-x", "-h", "-b",
                                        "bestand"};

    before();
    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(APPLICATIE)
                                     .setArgs(args)
                                     .setLocale(LOCALE).build();
    after();

    var errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals(Long.valueOf(230), parameterBundle.get("aantal"));
    assertEquals(d2112, (Date) parameterBundle.get("kort"));
    assertEquals("/tmp", (String) parameterBundle.get("uitdir"));
    assertFalse((Boolean) parameterBundle.get("exclude"));
    assertTrue((Boolean) parameterBundle.get("help"));
    assertEquals(1, errors.size());
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_PAR_AFWEZIG),
                             "--lang"), errors.get(0));
  }

  @Test
  public void testApplicatie3() {
    String[]  args      = new String[] {"-a", "230", "-k", "2112/12/21",
                                        "-u", "/tmp", "-x", "-h"};

    before();
    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(APPLICATIE)
                                     .setArgs(args)
                                     .setLocale(LOCALE).build();
    after();

    var errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals(Long.valueOf(230), parameterBundle.get("aantal"));
    assertEquals(d2112, (Date) parameterBundle.get("kort"));
    assertEquals("/tmp", (String) parameterBundle.get("uitdir"));
    assertFalse((Boolean) parameterBundle.get("exclude"));
    assertTrue((Boolean) parameterBundle.get("help"));
    assertEquals(1, errors.size());
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_PARS_AFWEZIG),
                             "-b", "--lang"), errors.get(0));
  }

  @Test
  public void testApplicatie4() {
    String[]  args      = new String[] {"--aantal", "\"230\"",
                                        "-k", "2112/12/21",
                                        "--uitvoerdir", "/tmp", "--exclude",
                                        "--help"};

    before();
    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(APPLICATIE)
                                     .setArgs(args)
                                     .setLocale(LOCALE).build();
    after();

    var errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals(Long.valueOf(230), parameterBundle.getLong("aantal"));
    assertEquals(d2112, parameterBundle.getDate("kort"));
    assertEquals("/tmp", parameterBundle.getString("uitdir"));
    assertFalse(parameterBundle.getBoolean("exclude"));
    assertTrue(parameterBundle.getBoolean("help"));
    assertEquals(1, errors.size());
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_PARS_AFWEZIG),
                             "-b", "--lang"), errors.get(0));
  }

  @Test
  public void testApplicatie5() {
    String[]  args      = new String[] {"--aantal=\"230\"", "-k", "2112/12/21",
                                        "--uitvoerdir=\"/tmp\"", "--exclude",
                                        "--help"};

    before();
    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(APPLICATIE)
                                     .setArgs(args)
                                     .setLocale(LOCALE).build();
    after();

    var errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals(Long.valueOf(230), parameterBundle.getLong("aantal"));
    assertEquals(d2112, parameterBundle.getDate("kort"));
    assertEquals(".", parameterBundle.getString("indir"));
    assertEquals("/tmp", parameterBundle.getString("uitdir"));
    assertFalse(parameterBundle.getBoolean("exclude"));
    assertTrue(parameterBundle.getBoolean("help"));
    assertEquals(1, errors.size());
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_PARS_AFWEZIG),
                             "-b", "--lang"), errors.get(0));
  }

  @Test
  public void testApplicatie6() {
    String[]  args      = new String[] {"--aantal=\"230\"", "-k", "2112/12/21",
                                        "--uitvoerdir=\"/tmp\"", "--exclude",
                                        "--help"};

    before();
    ParameterBundle parameterBundle =
        new ParameterBundle.Builder()
                           .setArgs(args)
                           .setBaseName(APPLICATIE)
                           .setLocale(LOCALE)
                           .setValidator(new ParameterBundleValidator())
                           .build();
    after();

    var errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals(Long.valueOf(230), parameterBundle.getLong("aantal"));
    assertEquals(d2112, parameterBundle.getDate("kort"));
    assertEquals(".", parameterBundle.getString("indir"));
    assertEquals("/tmp", parameterBundle.getString("uitdir"));
    assertFalse(parameterBundle.getBoolean("exclude"));
    assertTrue(parameterBundle.getBoolean("help"));
    assertEquals(2, errors.size());
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_PARS_AFWEZIG),
                             "-b", "--lang"), errors.get(0));
    assertEquals("PAR-9000 Datum ligt in de toekomst.", errors.get(1));
  }

  @Test
  public void testApplicatie7() {
    String[]  args      = new String[] {"--aantal=\"230\"", "-k", "2112/12/21",
                                        "--uitvoerdir=\"/tmp\"", "--exclude",
                                        "--help", "-b", TPY_BESTAND,
                                        "--csvbestand", "invoer",
                                        "--csv", "invoer.json",
                                        "--jsonbestand", "invoer.json"};

    before();
    ParameterBundle parameterBundle =
        new ParameterBundle.Builder()
                           .setArgs(args)
                           .setBaseName(APPLICATIE)
                           .setLocale(LOCALE)
                           .setValidator(new ParameterBundleValidator())
                           .build();
    after();

    var errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals(Long.valueOf(230), parameterBundle.getLong("aantal"));
    assertNull(parameterBundle.getBestand("invoer.csv"));
    assertEquals(TPY_BESTAND, parameterBundle.getBestand(TPY_BESTAND));
    assertEquals("invoer.json.csv", parameterBundle.getBestand("csv"));
    assertEquals("invoer.csv", parameterBundle.getBestand("csvbestand", "csv"));
    assertEquals("invoer.json", parameterBundle.getBestand("jsonbestand"));
    assertEquals(d2112, parameterBundle.getDate("kort"));
    assertEquals(".", parameterBundle.getString("indir"));
    assertEquals("/tmp", parameterBundle.getString("uitdir"));
    assertFalse(parameterBundle.getBoolean("exclude"));
    assertTrue(parameterBundle.getBoolean("help"));
    assertEquals(2, errors.size());
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_PAR_AFWEZIG),
                             "--lang"), errors.get(0));
    assertEquals("PAR-9000 Datum ligt in de toekomst.", errors.get(1));
  }

  private void testConfDubbel(String baseName, Locale locale, String dubbel) {
    before();
    ParameterBundle parameterBundle =
      new ParameterBundle.Builder().setBaseName(baseName)
                                   .setLocale(locale).build();

    parameterBundle.help();
    after();

    var errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals(1, errors.size());
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_CONF_DUBBEL),
                             dubbel), errors.get(0));
  }

  private void testConfDubbels(String baseName, Locale locale,
                               String dubbel1, String dubbel2) {
    before();
    ParameterBundle parameterBundle =
      new ParameterBundle.Builder().setBaseName(baseName)
                                   .setLocale(locale).build();

    parameterBundle.help();
    after();

    var errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals(1, errors.size());
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_CONFS_DUBBEL),
                             dubbel1, dubbel2), errors.get(0));
  }

  @Test
  public void testConfDubbel1() {
    testConfDubbel("dubbel1", LOCALE, "help");
  }

  @Test
  public void testConfDubbel2() {
    testConfDubbels("dubbel2", LOCALE, "help", "help1");
  }

  @Test
  public void testConfDubbel3() {
    testConfDubbels("dubbel3", LOCALE, "help, help1", "help2");
  }

  @Test
  public void testConfDubbel4() {
    testConfDubbel("locale", new Locale("fr"), "h");
  }

  @Test
  public void testConfDubbel5() {
    testConfDubbel("locale", new Locale("de"), "help");
  }

  @Test
  public void testHelp() {
    before();
    ParameterBundle parameterBundle =
      new ParameterBundle.Builder().setBaseName(APPLICATIE)
                                   .setLocale(LOCALE).build();
    after();

    before();
    parameterBundle.help();
    after();

    assertEquals(29, out.size());
  }

  @Test
  public void testInit1() {
    ParameterBundle parameterBundle;
    try {
      before();
      parameterBundle =
          new ParameterBundle.Builder().setBaseName(PARAMBUNDLE)
                                       .setLocale(LOCALE).build();
      after();
      fail("Er had een MissingResourceException moeten wezen.");
    } catch (MissingResourceException e) {
      assertEquals(MessageFormat.format(
          resourceBundle.getString(ERR_CONF_BESTAND), PARAMBUNDLE + EXT_JSON),
                   e.getMessage());
    }

    try {
      before();
      parameterBundle = new ParameterBundle.Builder().build();
      after();
      fail("Er had een MissingResourceException moeten wezen.");
    } catch (MissingResourceException e) {
      assertEquals(MessageFormat.format(
          resourceBundle.getString(ERR_CONF_BESTAND), PARAMBUNDLE + EXT_JSON),
                   e.getMessage());
    }
  }

  @Test
  public void testInit2() {
    before();
    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(PARAMETERS)
                                     .setLocale(LOCALE)
                                     .build();
    after();

    assertTrue(parameterBundle.isValid());
    assertFalse(parameterBundle.containsParameter(JSON_KEY_HELP));
    assertFalse(parameterBundle.containsParameter(PARAMETERS));
    assertEquals("Parameter Bundle", parameterBundle.getBannertekst());
    assertEquals(PARAMETERS, parameterBundle.getBaseName());
    assertEquals("Hiermee kunnen de parameters voor een applicatie worden gedefinieerd.",
                 parameterBundle.getHelp());
  }

  @Test
  public void testInit3() {
    before();
    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBanner(new MarcoBanner())
                                     .setBaseName(PARAMS)
                                     .setLocale(new Locale("nl-BE"))
                                     .build();
    after();

    assertTrue(parameterBundle.isValid());
    assertEquals("ParameterBundle", parameterBundle.getApplicatie());
  }

  @Test
  public void testInit4() {
    before();
    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBanner(new MarcoBanner())
                                     .setBaseName(PARAMS)
                                     .setLocale(new Locale("en"))
                                     .build();
    after();

    var errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals("ParameterBundle", parameterBundle.getApplicatie());
    assertEquals(2, errors.size());
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_PAR_ONBEKEND),
                             "helpEN", "en"), errors.get(0));
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_CONF_AFWEZIG),
                             JSON_KEY_BANNER), errors.get(1));
    assertEquals("en", parameterBundle.getLocale().toString());
  }

  @Test
  public void testInit5() {
    before();
    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBanner(new MarcoBanner())
                                     .setBaseName(PARAMS)
                                     .setLocale(new Locale("fr")).build();
    after();

    var errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals("ParameterBundle", parameterBundle.getApplicatie());
    assertEquals(1, errors.size());
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_CONF_AFWEZIG),
                             JSON_KEY_BANNER), errors.get(0));
    assertEquals("ParameterBundle: Applicatie: [ParameterBundle], "
                  + "Banner: [MarcoBanner], Bannertekst: [], BaseName: "
                  + "[params], Extrahelp: [null], Help: [L'aide.], Locale: "
                  + "[fr], Parameters: [[help: [kort: [h], lang: [help], "
                  + "standaard: [<null>], type: [string], format: [<null>], "
                  + "extensie: [<null>], verplicht: [true], waarde: [<null>], "
                  + "help: [Donnez ce texte d'aide.]]]",
                 parameterBundle.toString());
    assertEquals("fr", parameterBundle.getLocale().toString());
  }

  @Test
  public void testInit6() {
    String[]        args            = new String[] {"-k", "helptekst"};

    before();
    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(PARAMS)
                                     .setArgs(args)
                                     .setLocale(new Locale("en", "uk")).build();
    after();

    var errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals("ParameterBundle", parameterBundle.getApplicatie());
    assertEquals(1, errors.size());
    Locale.setDefault(new Locale("en", "uk"));
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_PAR_ONBEKEND),
                             "helpEN", "en_UK"), errors.get(0));
    assertEquals("en_UK", parameterBundle.getLocale().toString());
  }

  @Test
  public void testInit7() {
    String[]        args            = new String[] {"-k"};

    before();
    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(PARAMS)
                                     .setArgs(args)
                                     .setLocale(new Locale("en", "uk")).build();
    after();

    var errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals("ParameterBundle", parameterBundle.getApplicatie());
    assertEquals(1, errors.size());
    Locale.setDefault(new Locale("en", "uk"));
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_PAR_ONBEKEND),
                             "helpEN"), errors.get(0));
    assertEquals("en_UK", parameterBundle.getLocale().toString());
  }

  @Test
  public void testKort1() {
    testConfDubbel("kort1", LOCALE, "h");
  }

  @Test
  public void testLang1() {
    testConfDubbel("lang1", LOCALE, "hlp");
  }

  @Test
  public void testOnbekend1() {
    before();
    ParameterBundle parameterBundle =
      new ParameterBundle.Builder().setBaseName("locale")
                                   .setLocale(new Locale("en"))
                                   .build();

    parameterBundle.help();
    after();

    var errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals(1, errors.size());
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_PAR_ONBEKEND),
                             "info"), errors.get(0));
  }

  @Test
  public void testParamDubbel1() {
    String[]        args            = new String[] {"-u", "/tmp",
                                                    "-u", "/dev/null"};

    before();
    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName("applicatie")
                                     .setArgs(args)
                                     .build();
    after();

    var errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals(2, errors.size());
    assertEquals("PAR-0001", errors.get(0).split(" ")[0]);
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_PAR_DUBBEL),
                             "-u"), errors.get(1));
  }

  @Test
  public void testParamDubbel2() {
    String[]        args            = new String[] {"-u", "/tmp",
                                                    "-u", "/dev/null",
                                                    "-u", "/home"};

    before();
    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName("applicatie")
                                     .setArgs(args)
                                     .build();
    after();

    var errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals(2, errors.size());
    assertEquals("PAR-0001", errors.get(0).split(" ")[0]);
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_PAR_DUBBEL),
                             "-u"), errors.get(1));
  }

  @Test
  public void testParamDubbel3() {
    String[]        args            = new String[] {"-u", "/tmp",
                                                    "-u", "/dev/null",
                                                    "--jsonbestand", "json1",
                                                    "--jsonbestand", "json2"};

    before();
    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName("applicatie")
                                     .setArgs(args)
                                     .build();
    after();

    var errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals(2, errors.size());
    assertEquals("PAR-0001", errors.get(0).split(" ")[0]);
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_PARS_DUBBEL),
                             "-u", "--jsonbestand"), errors.get(1));
  }

  @Test
  public void testParamDubbel4() {
    String[]        args            = new String[] {"-u", "/tmp",
                                                    "-u", "/dev/null",
                                                    "--jsonbestand", "json1",
                                                    "--jsonbestand", "json2",
                                                    "--csv", "csv1",
                                                    "--csv", "csv2"};

    before();
    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName("applicatie")
                                     .setArgs(args)
                                     .build();
    after();

    var errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals("PAR-0001", errors.get(0).split(" ")[0]);
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_PARS_DUBBEL),
                             "--csv, -u", "--jsonbestand"), errors.get(1));
  }

  @Test
  public void testStandaard1() {
    String[]        args            = new String[] {"-h", "een helptekst"};

    before();
    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName("standaard1")
                                     .setArgs(args)
                                     .setLocale(LOCALE)
                                     .build();
    after();

    assertTrue(parameterBundle.isValid());
    assertEquals("een helptekst", (String) parameterBundle.get("versie"));
  }

  @Test
  public void testStandaard2() {
    String[]        args            = new String[] {"-h", "een helptekst"};

    before();
    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName("standaard2")
                                     .setArgs(args)
                                     .setLocale(LOCALE)
                                     .build();
    after();

    var             errors          = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals(1, errors.size());
    assertEquals("PAR-0206", errors.get(0).split(" ")[0]);
  }

  @Test
  public void testStandaard3() {
    String[]        args            = new String[] {"-h", "een helptekst"};

    before();
    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName("standaard3")
                                     .setArgs(args)
                                     .setLocale(LOCALE)
                                     .build();
    after();

    var             errors          = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals(1, errors.size());
    assertEquals("PAR-0205", errors.get(0).split(" ")[0]);
  }
}
