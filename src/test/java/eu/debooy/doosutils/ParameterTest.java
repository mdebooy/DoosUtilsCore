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

import java.util.Locale;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class ParameterTest {
  private static final JSONParser parser  = new JSONParser();

  @Test
  public void testInit1() {
    var         jParam  = "    {}";
    JSONObject  json    = null;
    try {
      json  = (JSONObject) parser.parse(jParam);
    } catch (ParseException e) {
      fail("Geen ParseException verwacht.");
    }
    var parameter = new Parameter(json);

    assertEquals("null: [kort: [<null>], lang: [<null>], standaard: [<null>], type: [string], format: [<null>], extensie: [<null>], verplicht: [false], waarde: [<null>], help: [<null>]", parameter.toString());
  }

  @Test
  public void testInit2() {
    var         jParam  = "    {\n" +
"      \"parameter\": \"kort\",\n" +
"      \"type\": \"Date\",\n" +
"    }";
    JSONObject  json    = null;
    try {
      json  = (JSONObject) parser.parse(jParam);
    } catch (ParseException e) {
      fail("Geen ParseException verwacht.");
    }
    var parameter = new Parameter(json);

    assertEquals("kort", parameter.getParameter());
    assertTrue(parameter.isValid());
    assertNull(parameter.getExtensie());
    assertNull(parameter.getFormat());
    assertNull(parameter.getHelp());
    assertNull(parameter.getKort());
    assertEquals("kort", parameter.getLang());
    assertNull(parameter.getStandaard());
    assertEquals("Date", parameter.getType());
    assertFalse(parameter.isVerplicht());
    assertNull(parameter.getWaarde());
  }

  @Test
  public void testInit3() {
    var         jParam  = "    {\n" +
"      \"format\": \"yyyy/MM/dd\",\n" +
"      \"parameter\": \"kort\",\n" +
"      \"standaard\": \"2112/12/21\",\n" +
"      \"type\": \"Date\",\n" +
"      \"verplicht\": true\n" +
"    }";
    JSONObject  json    = null;
    try {
      json  = (JSONObject) parser.parse(jParam);
    } catch (ParseException e) {
      fail("Geen ParseException verwacht.");
    }
    var parameter = new Parameter(json);

    assertEquals("kort: [kort: [<null>], lang: [kort], standaard: [<Date> Wed Dec 21 00:00:00 CET 2112], type: [Date], format: [yyyy/MM/dd], extensie: [<null>], verplicht: [true], waarde: [Wed Dec 21 00:00:00 CET 2112], help: [<null>]", parameter.toString());
    assertEquals("yyyy/MM/dd", parameter.getFormat());
  }

  @Test
  public void testInit4() {
    var         jParam  = "    {\n" +
"      \"parameter\": \"kort\",\n" +
"      \"extensie\": \"csv\",\n" +
"      \"standaard\": \"csvbestand\",\n" +
"      \"type\": \"bestand\",\n" +
"      \"verplicht\": true\n" +
"    }";
    JSONObject  json    = null;
    try {
      json  = (JSONObject) parser.parse(jParam);
    } catch (ParseException e) {
      fail("Geen ParseException verwacht.");
    }
    var parameter = new Parameter(json);

    assertEquals("kort", parameter.getParameter());
    assertTrue(parameter.isValid());
    assertEquals("csv", parameter.getExtensie());
    assertNull(parameter.getFormat());
    assertNull(parameter.getHelp());
    assertNull(parameter.getKort());
    assertEquals("kort", parameter.getLang());
    assertEquals("csvbestand", parameter.getStandaard());
    assertEquals("bestand", parameter.getType());
    assertTrue(parameter.isVerplicht());
    assertEquals("csvbestand", parameter.getWaarde());
  }

  @Test
  public void testInit5() {
    var         jParam  = "    {\n" +
                                "      \"parameter\": \"kort\",\n" +
                                "      \"standaard\": \"UTF-8\",\n" +
                                "      \"type\": \"charset\",\n" +
                                "      \"verplicht\": true\n" +
                                "    }";
    JSONObject  json    = null;
    try {
      json  = (JSONObject) parser.parse(jParam);
    } catch (ParseException e) {
      fail("Geen ParseException verwacht.");
    }

    var parameter = new Parameter(json);

    assertEquals("kort", parameter.getParameter());
    assertTrue(parameter.isValid());
    assertNull(parameter.getExtensie());
    assertNull(parameter.getFormat());
    assertNull(parameter.getHelp());
    assertNull(parameter.getKort());
    assertEquals("kort", parameter.getLang());
    assertEquals("UTF-8", parameter.getStandaard());
    assertEquals("charset", parameter.getType());
    assertTrue(parameter.isVerplicht());
    assertEquals("UTF-8", parameter.getWaarde());
  }

  @Test
  public void testInit6() {
    var         jParam  = "    {\n" +
"      \"parameter\": \"kort\",\n" +
"      \"type\": \"charset\",\n" +
"      \"verplicht\": true\n" +
"    }";
    JSONObject  json    = null;
    try {
      json  = (JSONObject) parser.parse(jParam);
    } catch (ParseException e) {
      fail("Geen ParseException verwacht.");
    }
    var parameter = new Parameter(json);

    assertEquals("kort", parameter.getParameter());
    assertTrue(parameter.isValid());
    assertNull(parameter.getExtensie());
    assertNull(parameter.getFormat());
    assertNull(parameter.getHelp());
    assertNull(parameter.getKort());
    assertEquals("kort", parameter.getLang());
    assertEquals("UTF-8", parameter.getStandaard());
    assertEquals("charset", parameter.getType());
    assertTrue(parameter.isVerplicht());
    assertEquals("UTF-8", parameter.getWaarde());
  }

  @Test
  public void testInit8() {
    var         jParam  = "    {\n" +
"      \"parameter\": \"kort\",\n" +
"      \"standaard\": \"nl-BE\",\n" +
"      \"type\": \"locale\",\n" +
"      \"verplicht\": true\n" +
"    }";
    JSONObject  json    = null;
    try {
      json  = (JSONObject) parser.parse(jParam);
    } catch (ParseException e) {
      fail("Geen ParseException verwacht.");
    }
    var parameter = new Parameter(json);

    assertEquals("kort", parameter.getParameter());
    assertTrue(parameter.isValid());
    assertNull(parameter.getExtensie());
    assertNull(parameter.getFormat());
    assertNull(parameter.getHelp());
    assertNull(parameter.getKort());
    assertEquals("kort", parameter.getLang());
    assertEquals("nl-BE", parameter.getStandaard());
    assertEquals("locale", parameter.getType());
    assertTrue(parameter.isVerplicht());
    assertEquals("nl-BE", parameter.getWaarde());
  }

  @Test
  public void testInit9() {
    Locale.setDefault(Locale.GERMAN);
    var         jParam  = "    {\n" +
"      \"parameter\": \"kort\",\n" +
"      \"type\": \"locale\",\n" +
"      \"verplicht\": true\n" +
"    }";
    JSONObject  json    = null;
    try {
      json  = (JSONObject) parser.parse(jParam);
    } catch (ParseException e) {
      fail("Geen ParseException verwacht.");
    }
    var parameter = new Parameter(json);

    assertEquals("kort", parameter.getParameter());
    assertTrue(parameter.isValid());
    assertNull(parameter.getExtensie());
    assertNull(parameter.getFormat());
    assertNull(parameter.getHelp());
    assertNull(parameter.getKort());
    assertEquals("kort", parameter.getLang());
    assertEquals("de", parameter.getStandaard());
    assertEquals("locale", parameter.getType());
    assertTrue(parameter.isVerplicht());
    assertEquals("de", parameter.getWaarde());
  }
}
