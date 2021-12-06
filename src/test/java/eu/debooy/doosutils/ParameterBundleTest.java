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
import static eu.debooy.doosutils.ParameterBundle.ERR_ARGS_AFWEZIG;
import static eu.debooy.doosutils.ParameterBundle.ERR_CONF_AFWEZIG;
import static eu.debooy.doosutils.ParameterBundle.ERR_CONF_BESTAND;
import static eu.debooy.doosutils.ParameterBundle.ERR_PAR_ONBEKEND;
import static eu.debooy.doosutils.ParameterBundle.JSON_KEY_BANNER;
import static eu.debooy.doosutils.ParameterBundle.PARAMBUNDLE;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
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
                                        "-u", "/temp", "-x", "-h"};

    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(APPLICATIE).build();
    parameterBundle.setArgs(args);
    String[]  errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals(Long.valueOf(230), parameterBundle.get("aantal"));
    assertEquals(d2112, (Date) parameterBundle.get("kort"));
    assertEquals("/temp", (String) parameterBundle.get("uitdir"));
    assertFalse((Boolean) parameterBundle.get("exclude"));
    assertTrue((Boolean) parameterBundle.get("help"));
    assertEquals(1, errors.length);
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_ARGS_AFWEZIG),
                             "-b", "--lang"), errors[0]);
  }

  @Test
  public void testApplicatie3() {
    Locale.setDefault(LOCALE);

    String[]  args      = new String[] {"--aantal", "\"230\"",
                                        "-k", "2112/12/21",
                                        "--uitvoerdir", "/temp", "--exclude",
                                        "--help"};

    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(APPLICATIE).build();
    parameterBundle.setArgs(args);
    String[]  errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals(Long.valueOf(230), parameterBundle.getLong("aantal"));
    assertEquals(d2112, parameterBundle.getDate("kort"));
    assertEquals("/temp", parameterBundle.getString("uitdir"));
    assertFalse(parameterBundle.getBoolean("exclude"));
    assertTrue(parameterBundle.getBoolean("help"));
    assertEquals(1, errors.length);
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_ARGS_AFWEZIG),
                             "-b", "--lang"), errors[0]);
  }

  @Test
  public void testApplicatie4() {
    Locale.setDefault(LOCALE);

    String[]  args      = new String[] {"--aantal=\"230\"", "-k", "2112/12/21",
                                        "--uitvoerdir=\"/temp\"", "--exclude",
                                        "--help"};

    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(APPLICATIE).build();
    parameterBundle.setArgs(args);
    String[]  errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals(Long.valueOf(230), parameterBundle.getLong("aantal"));
    assertEquals(d2112, parameterBundle.getDate("kort"));
    assertEquals(".", parameterBundle.getString("indir"));
    assertEquals("/temp", parameterBundle.getString("uitdir"));
    assertFalse(parameterBundle.getBoolean("exclude"));
    assertTrue(parameterBundle.getBoolean("help"));
    assertEquals(1, errors.length);
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_ARGS_AFWEZIG),
                             "-b", "--lang"), errors[0]);
  }

  @Test
  public void testApplicatie5() {
    Locale.setDefault(LOCALE);

    String[]  args      = new String[] {"--aantal=\"230\"", "-k", "2112/12/21",
                                        "--uitvoerdir=\"/temp\"", "--exclude",
                                        "--help"};

    ParameterBundle parameterBundle =
        new ParameterBundle.Builder()
                           .setBaseName(APPLICATIE)
                           .setValidator(new ParameterBundleValidator())
                           .build();
    parameterBundle.setArgs(args);
    String[]  errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals(Long.valueOf(230), parameterBundle.getLong("aantal"));
    assertEquals(d2112, parameterBundle.getDate("kort"));
    assertEquals(".", parameterBundle.getString("indir"));
    assertEquals("/temp", parameterBundle.getString("uitdir"));
    assertFalse(parameterBundle.getBoolean("exclude"));
    assertTrue(parameterBundle.getBoolean("help"));
    assertEquals(2, errors.length);
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_ARGS_AFWEZIG),
                             "-b", "--lang"), errors[0]);
    assertEquals("PAR-9000 Datum ligt in de toekomst.", errors[1]);
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
    String[]  errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals("ParameterBundle", parameterBundle.getApplicatie());
    assertEquals(2, errors.length);
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_PAR_ONBEKEND),
                             "helpEN", "en"), errors[0]);
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_CONF_AFWEZIG),
                             JSON_KEY_BANNER), errors[1]);
    assertEquals("en", parameterBundle.getLocale().toString());
  }

  @Test
  public void testInit5() {
    Locale.setDefault(new Locale("en", "uk"));

    String[]  args    = new String[] {"-k"};

    ParameterBundle parameterBundle =
        new ParameterBundle.Builder().setBaseName(PARAMS).build();
    parameterBundle.setArgs(args);
    String[]  errors  = parameterBundle.getErrors();

    assertFalse(parameterBundle.isValid());
    assertEquals("ParameterBundle", parameterBundle.getApplicatie());
    assertEquals(1, errors.length);
    assertEquals(
        MessageFormat.format(resourceBundle.getString(ERR_PAR_ONBEKEND),
                             "helpEN", "en_UK"), errors[0]);
    assertEquals("en_UK", parameterBundle.getLocale().toString());
  }
}
