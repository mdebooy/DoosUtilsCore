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

import static eu.debooy.doosutils.ParameterBundle.ERR_ARGS_AFWEZIG;
import static eu.debooy.doosutils.ParameterBundle.ERR_ARG_AFWEZIG;
import static eu.debooy.doosutils.ParameterBundle.ERR_ARG_FOUTIEF;
import static eu.debooy.doosutils.ParameterBundle.ERR_CONF_AFWEZIG;
import static eu.debooy.doosutils.ParameterBundle.ERR_CONF_BESTAND;
import static eu.debooy.doosutils.ParameterBundle.ERR_PAR_ONBEKEND;
import static eu.debooy.doosutils.ParameterBundle.EXT_JSON;
import static eu.debooy.doosutils.ParameterBundle.JSON_KEY_BANNER;
import static eu.debooy.doosutils.ParameterBundle.PARAMBUNDLE;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class ParameterBundleTest {
  protected static final  ClassLoader CLASSLOADER =
      ParameterBundleTest.class.getClassLoader();
  protected static final  Locale      LOCALE      = new Locale("nl");

  private static  ResourceBundle  resourceBundle;

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
    Locale.setDefault(LOCALE);

    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(APPLICATIE).build();

    assertTrue(parameterBundle.isValid());
  }

  @Test
  public void testApplicatie2() {
    Locale.setDefault(LOCALE);

    String[]  args      = new String[] {"-a", "230", "-k", "2112/12/21",
                                        "-u", "/tmp", "-x", "-h", "-b",
                                        "bestand"};

    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(APPLICATIE).build();
    parameterBundle.setArgs(args);
    var errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals(Long.valueOf(230), parameterBundle.get("aantal"));
    assertEquals(d2112, (Date) parameterBundle.get("kort"));
    assertEquals("/tmp", (String) parameterBundle.get("uitdir"));
    assertFalse((Boolean) parameterBundle.get("exclude"));
    assertTrue((Boolean) parameterBundle.get("help"));
    assertEquals(1, errors.size());
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_ARG_AFWEZIG),
                             "--lang"), errors.get(0));
  }

  @Test
  public void testApplicatie3() {
    Locale.setDefault(LOCALE);

    String[]  args      = new String[] {"-a", "230", "-k", "2112/12/21",
                                        "-u", "/tmp", "-x", "-h"};

    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(APPLICATIE).build();
    parameterBundle.setArgs(args);
    var errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals(Long.valueOf(230), parameterBundle.get("aantal"));
    assertEquals(d2112, (Date) parameterBundle.get("kort"));
    assertEquals("/tmp", (String) parameterBundle.get("uitdir"));
    assertFalse((Boolean) parameterBundle.get("exclude"));
    assertTrue((Boolean) parameterBundle.get("help"));
    assertEquals(1, errors.size());
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_ARGS_AFWEZIG),
                             "-b", "--lang"), errors.get(0));
  }

  @Test
  public void testApplicatie4() {
    Locale.setDefault(LOCALE);

    String[]  args      = new String[] {"--aantal", "\"230\"",
                                        "-k", "2112/12/21",
                                        "--uitvoerdir", "/tmp", "--exclude",
                                        "--help"};

    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(APPLICATIE).build();
    parameterBundle.setArgs(args);
    var errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals(Long.valueOf(230), parameterBundle.getLong("aantal"));
    assertEquals(d2112, parameterBundle.getDate("kort"));
    assertEquals("/tmp", parameterBundle.getString("uitdir"));
    assertFalse(parameterBundle.getBoolean("exclude"));
    assertTrue(parameterBundle.getBoolean("help"));
    assertEquals(1, errors.size());
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_ARGS_AFWEZIG),
                             "-b", "--lang"), errors.get(0));
  }

  @Test
  public void testApplicatie5() {
    Locale.setDefault(LOCALE);

    String[]  args      = new String[] {"--aantal=\"230\"", "-k", "2112/12/21",
                                        "--uitvoerdir=\"/tmp\"", "--exclude",
                                        "--help"};

    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(APPLICATIE).build();
    parameterBundle.setArgs(args);
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
        MessageFormat.format(resourceBundle.getString(ERR_ARGS_AFWEZIG),
                             "-b", "--lang"), errors.get(0));
  }

  @Test
  public void testApplicatie6() {
    Locale.setDefault(LOCALE);

    String[]  args      = new String[] {"--aantal=\"230\"", "-k", "2112/12/21",
                                        "--uitvoerdir=\"/tmp\"", "--exclude",
                                        "--help"};

    ParameterBundle parameterBundle =
        new ParameterBundle.Builder()
                           .setBaseName(APPLICATIE)
                           .setValidator(new ParameterBundleValidator())
                           .build();
    parameterBundle.setArgs(args);
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
        MessageFormat.format(resourceBundle.getString(ERR_ARGS_AFWEZIG),
                             "-b", "--lang"), errors.get(0));
    assertEquals("PAR-9000 Datum ligt in de toekomst.", errors.get(1));
  }

  @Test
  public void testHelp() {
    var errContent  = new ByteArrayOutputStream();
    var outContent  = new ByteArrayOutputStream();

    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));

    ParameterBundle parameterBundle =
      new ParameterBundle.Builder().setBaseName(APPLICATIE)
                                   .setLocale(LOCALE).build();
    parameterBundle.help();

    var help  = outContent.toString().split("\\n");

    System.setErr(new PrintStream(System.err));
    System.setOut(new PrintStream(System.out));

    assertEquals(22, help.length);
  }

  @Test
  public void testInit1() {
    Locale.setDefault(LOCALE);

    ParameterBundle parameterBundle;
    try {
      parameterBundle = new ParameterBundle.Builder().setBaseName(PARAMBUNDLE)
                                           .build();
      fail("Er had een MissingResourceException moeten wezen.");
    } catch (MissingResourceException e) {
      assertEquals(MessageFormat.format(
          resourceBundle.getString(ERR_CONF_BESTAND), PARAMBUNDLE + EXT_JSON),
                   e.getMessage());
    }

    try {
      parameterBundle = new ParameterBundle.Builder().build();
      fail("Er had een MissingResourceException moeten wezen.");
    } catch (MissingResourceException e) {
      assertEquals(MessageFormat.format(
          resourceBundle.getString(ERR_CONF_BESTAND), PARAMBUNDLE + EXT_JSON),
                   e.getMessage());
    }
  }

  @Test
  public void testInit2() {
    Locale.setDefault(LOCALE);

    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(PARAMETERS).build();
    assertTrue(parameterBundle.isValid());
  }

  @Test
  public void testInit3() {
    Locale.setDefault(new Locale("nl-BE"));

    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(PARAMS).build();

    assertTrue(parameterBundle.isValid());
    assertEquals("ParameterBundle", parameterBundle.getApplicatie());
  }

  @Test
  public void testInit4() {
    Locale.setDefault(new Locale("en"));

    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(PARAMS).build();
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
    Locale.setDefault(new Locale("nl"));

    String[]  args    = new String[] {"-k", "helptekst"};

    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(PARAMS)
                                     .setLocale(new Locale("en", "uk")).build();
    parameterBundle.setArgs(args);
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
  public void testInit6() {
    Locale.setDefault(new Locale("nl"));

    String[]  args    = new String[] {"-k"};

    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(PARAMS)
                                     .setLocale(new Locale("en", "uk")).build();
    parameterBundle.setArgs(args);
    var errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals("ParameterBundle", parameterBundle.getApplicatie());
    assertEquals(2, errors.size());
    Locale.setDefault(new Locale("en", "uk"));
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_PAR_ONBEKEND),
                             "helpEN", "en_UK"), errors.get(0));
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_ARG_FOUTIEF),
                             "-k"), errors.get(1));
    assertEquals("en_UK", parameterBundle.getLocale().toString());
  }
}
