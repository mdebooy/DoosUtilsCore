/*
 * Copyright (c) 2024 Marco de Booij
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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class IBannerTest {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;

  private IBanner banner;

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
  public void printDoosBannerTest() {
    banner  = new DoosBanner();

    banner.print("printDoosBannerTest");

    var uitvoer = outContent.toString().split("\\n");

    assertEquals(10, uitvoer.length);
    assertTrue(uitvoer[4].endsWith("printDoosBannerTest"));
    assertEquals(0, errContent.size());
  }

  @Test
  public void printMarcoBannerTest() {
    banner  = new MarcoBanner();

    banner.print("printMarcoBannerTest");

    var uitvoer = outContent.toString().split("\\n");

    assertEquals(12, uitvoer.length);
    assertTrue(uitvoer[2].endsWith("printMarcoBannerTest"));
    assertEquals(0, errContent.size());
  }
}
