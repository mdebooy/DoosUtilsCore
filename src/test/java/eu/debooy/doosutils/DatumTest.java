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
import junit.framework.TestCase;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class DatumTest extends TestCase {
  @Test
  public void testToDate1() {
    try {
      var datum = Datum.toDate("01/01/2010");
    } catch (ParseException e) {
      assertTrue(e.getMessage(), true);
    }
  }

  @Test
  public void testToDate2() {
    try {
      var datum = Datum.toDate("01/01/2010 01:01:01",
                               DoosConstants.DATUM_TIJD);
    } catch (ParseException e) {
      assertTrue(e.getMessage(), true);
    }
  }

  @Test
  public void testToDate3() {
    try {
      var datum = Datum.toDate("01:01:2010", DoosConstants.DATUM);
    } catch (ParseException e) {
      assertTrue(e.getMessage(), true);
    }
  }

  @Test
  public void testFromDate1() {
    try {
      var datum       = Datum.toDate("01/01/2010");
      var datumString = Datum.fromDate(datum);
    } catch (ParseException e) {
      assertTrue(e.getMessage(), true);
    }
  }

  @Test
  public void testFromDate2() {
    try {
      var datum       = Datum.toDate("01/01/2010 01:01:01",
                               DoosConstants.DATUM_TIJD);
      var datumString = Datum.fromDate(datum, DoosConstants.DATUM_TIJD);
    } catch (ParseException e) {
      assertTrue(e.getMessage(), true);
    }
  }
}
