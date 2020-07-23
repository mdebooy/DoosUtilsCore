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

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author Marco de Booij
 */
public final class Datum {
  private static  Format  datumFormaat  = null;

  private Datum() {}

  public static String fromDate(Date datum) throws ParseException {
    return fromDate(datum, DoosConstants.DATUM);
  }

  public static String fromDate(Date datum, String formaat) {
    if (null == datum) {
      return null;
    }

    datumFormaat  = new SimpleDateFormat(formaat);

    return datumFormaat.format(datum);
  }

  public static Date toDate(String datum) throws ParseException {
    return toDate(datum, DoosConstants.DATUM);
  }

  public static Date toDate(String datum, String formaat)
      throws ParseException {
    if (DoosUtils.isBlankOrNull(datum)) {
      return null;
    }

    datumFormaat  = new SimpleDateFormat(formaat);

    return (Date) datumFormaat.parseObject(datum);
  }
}
