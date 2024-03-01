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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class DoosUtilsTest {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;

  private final String  TEKST   = "tekst";
  private final String  TEKSTU  = "TEKST";

  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @After
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  @Test
  public void findGettersTest() {
    var methodes  = DoosUtils.findGetters(this.getClass().getMethods());

    // Ook de getClass zit erbij.
    assertEquals(8, methodes.size());
  }

  @Test
  public void foutNaarSchermTest() {
    DoosUtils.foutNaarScherm("Fout");

    assertEquals(0, outContent.size());
    assertEquals("Fout\n", errContent.toString());
  }

  @Test
  public void getEolTest() {
    assertEquals(System.getProperty("line.separator"), DoosUtils.getEol());
  }

  @Test
  public void getFileSepTest() {
    assertEquals(System.getProperty("file.separator"), DoosUtils.getFileSep());
  }

  @Test
  public void getWachtwoordTest() {
    var sysInBackup = System.in;
    var in          = new ByteArrayInputStream("geheim".getBytes());
    System.setIn(in);

    var wachtwoord  = DoosUtils.getWachtwoord("wachtwoord: ");
    assertEquals("geheim", wachtwoord);

    System.setIn(sysInBackup);
  }

  @Test
  public void isBlankOrNullTest() {
    assertTrue(DoosUtils.isBlankOrNull(null));
    assertTrue(DoosUtils.isBlankOrNull(""));
    assertTrue(DoosUtils.isBlankOrNull(" "));
    assertFalse(DoosUtils.isBlankOrNull("x"));
  }

  @Test
  public void isFalseTest() {
    assertTrue(DoosUtils.isFalse(DoosConstants.ONWAAR));
    assertFalse(DoosUtils.isFalse(DoosConstants.WAAR));
  }

  @Test
  public void isNotBlankOrNullTest() {
    assertFalse(DoosUtils.isNotBlankOrNull(null));
    assertFalse(DoosUtils.isNotBlankOrNull(""));
    assertFalse(DoosUtils.isNotBlankOrNull(" "));
    assertTrue(DoosUtils.isNotBlankOrNull("x"));
  }

  @Test
  public void isTrueTest() {
    assertTrue(DoosUtils.isTrue(DoosConstants.WAAR));
    assertFalse(DoosUtils.isTrue(DoosConstants.ONWAAR));
  }

  @Test
  public void naarScherm1Test() {
    DoosUtils.naarScherm();

    assertEquals("\n", outContent.toString());
    assertEquals(0, errContent.size());
  }

  @Test
  public void naarScherm2Test() {
    DoosUtils.naarScherm("Test");

    assertEquals("Test\n", outContent.toString());
    assertEquals(0, errContent.size());
  }

  @Test
  public void naarScherm3Test() {
    DoosUtils.naarScherm("test test test", 5);

    var regel = outContent.toString().split("\\n");

    assertEquals(3, regel.length);
    assertEquals("test", regel[0]);
    assertEquals(0, errContent.size());
  }

  @Test
  public void naarScherm4Test() {
    DoosUtils.naarScherm("-", "test test test", 6);

    var regel = outContent.toString().split("\\n");

    assertEquals(3, regel.length);
    assertEquals("-test", regel[0]);
    assertEquals(" test", regel[1]);
    assertEquals(0, errContent.size());
  }

  @Test
  public void nullToEmptyTest() {
    assertEquals("",  DoosUtils.nullToEmpty(null));
    assertEquals("",  DoosUtils.nullToEmpty(""));
    assertEquals("x", DoosUtils.nullToEmpty("x"));
  }

  @Test
  public void nullToValueDoubleTest() {
    Double  defaultWaarde = 10.3;
    Double  waarde        = null;
    assertEquals(defaultWaarde,  DoosUtils.nullToValue(waarde, defaultWaarde));
    waarde  = 10.3;
    assertEquals(waarde, DoosUtils.nullToValue(waarde, defaultWaarde));
  }

  @Test
  public void nullToValueIntegerTest() {
    Integer defaultWaarde = 10;
    Integer waarde        = null;
    assertEquals(defaultWaarde,  DoosUtils.nullToValue(waarde, defaultWaarde));
    waarde  = 11;
    assertEquals(waarde, DoosUtils.nullToValue(waarde, defaultWaarde));
  }

  @Test
  public void nullToValueLongTest() {
    Long  defaultWaarde = 10L;
    Long  waarde        = null;
    assertEquals(defaultWaarde,  DoosUtils.nullToValue(waarde, defaultWaarde));
    waarde  = 11L;
    assertEquals(waarde, DoosUtils.nullToValue(waarde, defaultWaarde));
  }

  @Test
  public void nullToValueStringTest() {
    assertEquals("x",  DoosUtils.nullToValue(null, "x"));
    assertEquals("",  DoosUtils.nullToValue("", "x"));
    assertEquals("a", DoosUtils.nullToValue("a", "x"));
  }

  @Test
  public void stringMetLengte1Test() {
    assertEquals(10, DoosUtils.stringMetLengte("", 10).length());
    assertEquals("          ", DoosUtils.stringMetLengte("", 10));
    assertEquals("      test", DoosUtils.stringMetLengte("test", 10));
    assertEquals(10, DoosUtils.stringMetLengte("abcdefghijklm", 10).length());
  }

  @Test
  public void stringMetLengte2Test() {
    assertEquals(10, DoosUtils.stringMetLengte("", 10, "xyz").length());
    assertEquals("xyzxyzxyzx", DoosUtils.stringMetLengte("", 10, "xyz"));
    assertEquals("testxyzxyz", DoosUtils.stringMetLengte("test", 10, "xyz"));
    assertEquals(10, DoosUtils.stringMetLengte("abcdefghijklm", 10, "xyz")
                              .length());
    assertEquals("abcdefghij", DoosUtils.stringMetLengte("abcdefghijklm", 10,
                                                         "xyz"));
  }

  @Test
  public void stripBeginEnEindTest1() {
    assertEquals(TEKST, DoosUtils.stripBeginEnEind(TEKST, "x"));
  }

  @Test
  public void stripBeginEnEindTest2() {
    assertEquals("eks", DoosUtils.stripBeginEnEind(TEKST, "t"));
  }

  @Test
  public void stripBeginEnEindTest3() {
    assertEquals("k", DoosUtils.stripBeginEnEind(TEKST, "te", "st"));
  }

  @Test
  public void stripTest1() {
    assertNull(DoosUtils.strip(null));
  }

  @Test
  public void stripTest2() {
    assertEquals(TEKST, DoosUtils.strip(TEKST + "  "));
  }

  @Test
  public void stripTest3() {
    assertEquals(TEKST, DoosUtils.strip("  " + TEKST));
  }

  @Test
  public void stripTest4() {
    assertEquals(TEKST, DoosUtils.strip("  " + TEKST + "  "));
  }

  @Test
  public void stripTest5() {
    assertEquals(TEKST, DoosUtils.strip("\t" + TEKST + "\t"));
  }

  @Test
  public void stripTest6() {
    assertEquals(TEKST, DoosUtils.strip("\t" + TEKST + "\n"));
  }

  @Test
  public void stripTest7() {
    assertEquals("", DoosUtils.strip("\t \n"));
  }

  @Test
  public void stripToLowercaseTest1() {
    assertNull(DoosUtils.strip(null));
  }

  @Test
  public void stripToLowercaseTest2() {
    assertEquals(TEKST, DoosUtils.stripToLowercase(TEKSTU + "  "));
  }

  @Test
  public void stripToLowercaseTest3() {
    assertEquals(TEKST, DoosUtils.stripToLowercase("  " + TEKSTU));
  }

  @Test
  public void stripToLowercaseTest4() {
    assertEquals(TEKST, DoosUtils.stripToLowercase("  " + TEKSTU + "  "));
  }

  @Test
  public void stripToLowercaseTest5() {
    assertEquals(TEKST, DoosUtils.stripToLowercase("\t" + TEKSTU + "\t"));
  }

  @Test
  public void stripToLowercaseTest6() {
    assertEquals(TEKST, DoosUtils.stripToLowercase("\t" + TEKSTU + "\n"));
  }

  @Test
  public void stripToLowercaseTest7() {
    assertEquals("", DoosUtils.stripToLowercase("\t \n"));
  }

  @Test
  public void stripToUppercaseTest1() {
    assertNull(DoosUtils.strip(null));
  }

  @Test
  public void stripToUppercaseTest2() {
    assertEquals(TEKSTU, DoosUtils.stripToUppercase(TEKST + "  "));
  }

  @Test
  public void stripToUppercaseTest3() {
    assertEquals(TEKSTU, DoosUtils.stripToUppercase("  " + TEKST));
  }

  @Test
  public void stripToUppercaseTest4() {
    assertEquals(TEKSTU, DoosUtils.stripToUppercase("  " + TEKST + "  "));
  }

  @Test
  public void stripToUppercaseTest5() {
    assertEquals(TEKSTU, DoosUtils.stripToUppercase("\t" + TEKST + "\t"));
  }

  @Test
  public void stripToUppercaseTest6() {
    assertEquals(TEKSTU, DoosUtils.stripToUppercase("\t" + TEKST + "\n"));
  }

  @Test
  public void stripToUppercaseTest7() {
    assertEquals("", DoosUtils.stripToLowercase("\t \n"));
  }

  @Test
  public void telTekenTest() {
    assertEquals(0, DoosUtils.telTeken("xyz", 'a'));
    assertEquals(4, DoosUtils.telTeken("xyzxyzxyzx", 'x'));
    assertEquals(3, DoosUtils.telTeken("xyzxyzxyzx", 'y'));
  }

  @Test
  public void testUniekeCharacters() {
    assertEquals("xyz",     DoosUtils.uniekeCharacters("xyzxyzxyzx"));
    assertEquals("tesxyz",  DoosUtils.uniekeCharacters("testxyzxyz"));
    assertEquals(" tes",    DoosUtils.uniekeCharacters("      test"));
  }
}
