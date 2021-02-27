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

import java.util.Date;
import static junit.framework.TestCase.assertEquals;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class MailDataTest {
  public static final String  SIMPLE_MAILDATA =
      "{contentType=text/html from=from@junit.tst to=to@junit.tst "
      + "cc=cc@unit.tst bcc=bcc@junit.tst header=value "
      + "sentDate=Thu Jan 01 01:00:00 CET 1970 subject=subject "
      + "message=message}";

  @Test
  public void testMailData1() {
    MailData mailData = new MailData();
    mailData.setBcc("bcc@junit.tst");
    mailData.setCc("cc@unit.tst");
    mailData.setContentType("text/html");
    mailData.setFrom("from@junit.tst");
    mailData.setHeader("type", "value");
    mailData.setMessage("message");
    mailData.setSentDate(new Date(1));
    mailData.setSubject("subject");
    mailData.setTo("to@junit.tst");
    assertEquals(SIMPLE_MAILDATA, mailData.toString());
    assertEquals(-385379184, mailData.hashCode());
  }

  @Test
  public void testMailData2() {
    MailData mailData = new MailData();
    mailData.addBcc("bcc@junit.tst");
    mailData.addCc("cc@unit.tst");
    mailData.setContentType("text/html");
    mailData.setFrom("from@junit.tst");
    mailData.addHeader("type", "value");
    mailData.setMessage("message");
    mailData.setSentDate(new Date(1));
    mailData.setSubject("subject");
    mailData.addTo("to@junit.tst");
    assertEquals(SIMPLE_MAILDATA, mailData.toString());
    assertEquals(-385379184, mailData.hashCode());
  }
}
