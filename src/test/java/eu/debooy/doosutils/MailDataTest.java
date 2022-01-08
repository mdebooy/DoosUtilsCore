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
import java.util.HashMap;
import java.util.Map;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class MailDataTest {
  private static final String  SIMPLE_MAILDATA =
      "{contentType=text/html from=from@junit.tst to=to@junit.tst "
      + "cc=cc@unit.tst bcc=bcc@junit.tst header=value "
      + "sentDate=Thu Jan 01 01:00:00 CET 1970 subject=subject "
      + "message=message}";
  private static final  String  DEF_CONTENTTYPE = "text/html; charset=UTF-8";
  private static final  String  TST_CONTENTTYPE = "content";

  @Test
  public void testContentType() {
    var mailData  = new MailData();

    mailData.setContentType(TST_CONTENTTYPE);
    assertEquals(TST_CONTENTTYPE, mailData.getContentType());
    mailData.setContentType(null);
    assertEquals(DEF_CONTENTTYPE, mailData.getContentType());
    mailData.setContentType(TST_CONTENTTYPE);
    assertEquals(TST_CONTENTTYPE, mailData.getContentType());
    mailData.setContentType("");
    assertEquals(DEF_CONTENTTYPE, mailData.getContentType());
    mailData.setContentType(TST_CONTENTTYPE);
    assertEquals(TST_CONTENTTYPE, mailData.getContentType());
    mailData.setContentType("     ");
    assertEquals(DEF_CONTENTTYPE, mailData.getContentType());
  }

  @Test
  public void testEquals() {
    var mailData1 = new MailData();
    var mailData2 = new MailData();

    mailData1.setSubject("mailData1");
    mailData1.setSentDate(new Date(1));
    mailData2.setSubject("mailData2");
    mailData2.setSentDate(new Date(1));

    assertNotEquals(null, mailData1);
    assertNotEquals(mailData1, null);
    assertEquals(mailData1, mailData1);
    assertNotEquals("", mailData1);
    assertNotEquals(mailData2, mailData1);
    mailData2.setSubject("mailData1");
    assertEquals(mailData2, mailData1);
  }

  @Test
  public void testMailData1() {
    var mailData  = new MailData();

    mailData.setBcc("bcc@junit.tst");
    mailData.setCc("cc@unit.tst");
    mailData.setContentType("text/html");
    mailData.setFrom("from@junit.tst");
    mailData.setHeader("type", "value");
    mailData.setMessage("message");
    mailData.setSentDate(new Date(1));
    mailData.setSubject("subject");
    mailData.setTo("to@junit.tst");

    assertEquals(1, mailData.getBccSize());
    assertEquals(1, mailData.getCcSize());
    assertEquals("from@junit.tst", mailData.getFrom());
    assertEquals(1, mailData.getHeaderSize());
    assertEquals("message", mailData.getMessage());
    assertEquals("subject", mailData.getSubject());
    assertEquals(1, mailData.getToSize());
    assertEquals(-385379184, mailData.hashCode());
    assertEquals(SIMPLE_MAILDATA, mailData.toString());
  }

  @Test
  public void testMailData2() {
    var mailData  = new MailData();
    mailData.addBcc("bcc@junit.tst");
    mailData.addCc("cc@unit.tst");
    mailData.setContentType("text/html");
    mailData.setFrom("from@junit.tst");
    mailData.addHeader("type", "value");
    mailData.setMessage("message");
    mailData.setSentDate(new Date(1));
    mailData.setSubject("subject");
    mailData.addTo("to@junit.tst");
    assertEquals(1, mailData.getBccSize());
    assertEquals(1, mailData.getCcSize());
    assertEquals(1, mailData.getHeaderSize());
    assertEquals(1, mailData.getToSize());
    assertEquals(-385379184, mailData.hashCode());
    assertEquals(SIMPLE_MAILDATA, mailData.toString());
  }

  @Test
  public void testMailData3() {
    Map<String, String> adressen  = new HashMap<>();
    var                 mailData  = new MailData();

    adressen.put("adres1@junit.tst", "");
    adressen.put("adres2@junit.tst", "Adres 2");

    mailData.setBcc(adressen);
    mailData.setCc(adressen);
    mailData.setHeader(adressen);
    mailData.setTo(adressen);

    assertEquals(2, mailData.getBccSize());
    assertEquals(2, mailData.getCcSize());
    assertEquals(2, mailData.getHeaderSize());
    assertEquals(2, mailData.getToSize());
    assertEquals(adressen, mailData.getBcc());
    assertEquals(adressen, mailData.getCc());
    assertEquals(adressen, mailData.getHeader());
    assertEquals(adressen, mailData.getTo());
  }

  @Test
  public void testSendDate() {
    var mailData  = new MailData();
    assertNotNull(mailData.getSentDate());

    mailData.setSentDate(new Date(1));
    mailData.setSentDate(null);

    assertNotEquals(new Date(1), mailData.getSentDate());
    assertNotNull(mailData.getSentDate());
  }
}
