/**
 * Copyright 2010 Marco de Booy
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.doosutils;

import java.text.ParseException;
import java.util.Date;
import junit.framework.TestCase;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class DatumTest extends TestCase {
  public static final String  FORMAAT   = "yyyy-MM-dd";
  public static final String  RUSHDATUM = "2112-21-12";
  public static final String  VANDAAG   = Datum.fromDate(new Date(), FORMAAT);

  @Test
  public void testFromDate1() {
    String  datumString = null;
    try {
      var datum   = Datum.toDate("01-01-2010");
      datumString = Datum.fromDate(datum);
    } catch (ParseException e) {
      fail("Geen ParseException verwacht.");
    }

    assertEquals("01-01-2010", datumString);
  }

  @Test
  public void testFromDate2() {
    String  datumString = null;
    try {
      var datum   = Datum.toDate("01-01-2010 01:01:01",
                               DoosConstants.DATUM_TIJD);
      datumString = Datum.fromDate(datum, DoosConstants.DATUM_TIJD);
    } catch (ParseException e) {
      fail("Geen ParseException verwacht.");
    }

    assertEquals("01-01-2010 01:01:01", datumString);
  }

  @Test
  public void testFromDate3() {
    var datumString = Datum.fromDate(null, DoosConstants.DATUM_TIJD);

    assertNull(datumString);
  }

  @Test
  public void testMax1() {
    var datum = Datum.max(RUSHDATUM, VANDAAG);

    assertEquals(RUSHDATUM, datum);

    datum = Datum.max(VANDAAG, RUSHDATUM);

    assertEquals(RUSHDATUM, datum);
  }

  @Test
  public void testMax2() throws ParseException {
    var datum = Datum.max(Datum.toDate(RUSHDATUM, FORMAAT),
                          Datum.toDate(VANDAAG, FORMAAT));

    assertEquals(Datum.toDate(RUSHDATUM, FORMAAT), datum);

    datum = Datum.max(Datum.toDate(VANDAAG, FORMAAT),
                      Datum.toDate(RUSHDATUM, FORMAAT));

    assertEquals(Datum.toDate(RUSHDATUM, FORMAAT), datum);
  }

  @Test
  public void testMin1() {
    var datum = Datum.min(RUSHDATUM, VANDAAG);

    assertEquals(VANDAAG, datum);

    datum = Datum.min(VANDAAG, RUSHDATUM);

    assertEquals(VANDAAG, datum);
  }

  @Test
  public void testMin2() throws ParseException {
    var datum = Datum.min(Datum.toDate(RUSHDATUM, FORMAAT),
                          Datum.toDate(VANDAAG, FORMAAT));

    assertEquals(Datum.toDate(VANDAAG, FORMAAT), datum);

    datum = Datum.min(Datum.toDate(VANDAAG, FORMAAT),
                      Datum.toDate(RUSHDATUM, FORMAAT));

    assertEquals(Datum.toDate(VANDAAG, FORMAAT), datum);
  }

  @Test
  public void testStripTime1() {
    assertNull(Datum.stripTime(null));
  }

  @Test
  public void testStripTime2() {
    var datumTijd = new Date();
    var tekst     = Datum.fromDate(datumTijd);
    var datum     = Datum.stripTime(datumTijd);

    assertNotEquals(datumTijd, datum);
    assertEquals(tekst, Datum.fromDate(datumTijd));
  }

  @Test
  public void testToDate1() {
    try {
      var datum = Datum.toDate("01-01-2010");
    } catch (ParseException e) {
      fail("Geen ParseException verwacht.");
    }
  }

  @Test
  public void testToDate2() {
    try {
      var datum = Datum.toDate("01-01-2010 01:01:01",
                               DoosConstants.DATUM_TIJD);
    } catch (ParseException e) {
      fail("Geen ParseException verwacht.");
    }
  }

  @Test
  public void testToDate3() {
    try {
      var datum = Datum.toDate("01:01:2010", DoosConstants.DATUM);
      fail("Er had een ParseException moeten wezen.");
    } catch (ParseException e) {
      assertEquals("Format.parseObject(String) failed", e.getMessage());
    }
  }

  @Test
  public void testToDate4() {
    var datum = new Date();
    try {
      datum = Datum.toDate(null, DoosConstants.DATUM);
    } catch (ParseException e) {
      fail("Geen ParseException verwacht.");
    }

    assertNull(datum);
  }
}
