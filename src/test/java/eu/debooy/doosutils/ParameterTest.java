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

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class ParameterTest {
  private static JSONParser parser  = new JSONParser();

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
"      \"verplicht\": true\n" +
"    }";
    JSONObject  json    = null;
    try {
      json  = (JSONObject) parser.parse(jParam);
    } catch (ParseException e) {
      fail("Geen ParseException verwacht.");
    }
    var parameter = new Parameter(json);

    assertEquals("kort: [kort: [<null>], lang: [kort], standaard: [<null>], type: [Date], format: [<null>], extensie: [<null>], verplicht: [true], waarde: [<null>], help: [<null>]", parameter.toString());
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
}
