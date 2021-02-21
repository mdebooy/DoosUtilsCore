/**
 * Copyright (c) 2009 Marco de Booy
 *
 * Licensed under the EUPL, Version 1.0 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/7330l5
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package eu.debooy.doosutils;

import junit.framework.TestCase;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class ArgumentsTest extends TestCase {
  private static final  String  DELETE        = "delete";
  private static final  String  MINDELETE     = "-delete";
  private static final  String  MINMINDELETE  = "--delete";
  private static final  String  FALSE         = "false";
  private static final  String  INSERT        = "insert";
  private static final  String  MININSERT     = "-insert";
  private static final  String  MINMININSERT  = "--insert";
  private static final  String  TRUE          = "true";
  private static final  String  UPDATE        = "update";

  String[]  args1 = {MINDELETE};
  String[]  args2 = {MINDELETE + "=" + TRUE};
  String[]  args3 = {MINDELETE, TRUE};
  String[]  args4 = {MINMINDELETE};
  String[]  args5 = {MINMINDELETE + "=" + TRUE};
  String[]  args6 = {MINMINDELETE, TRUE};
  String[]  args7 = {MINDELETE, MININSERT};
  String[]  args8 = {MINDELETE, TRUE, MININSERT, TRUE};
  String[]  args9 = {MINDELETE};

  @Test
  public void testInitialize() {
    String[]  args      = {MINDELETE, TRUE, MININSERT};
    Arguments arguments = new Arguments(args);

    assertTrue(arguments.isValid());
    assertTrue(arguments.hasArgument(DELETE));
    assertEquals(TRUE, arguments.getArgument(DELETE));
    assertTrue(arguments.hasArgument(INSERT));
  }

  @Test
  public void testZonderMinZonderWaarde() {
    Arguments arguments = new Arguments();
    String[]  args      = {DELETE};

    assertFalse(arguments.setArguments(args));
  }

  @Test
  public void testZonderMinMetWaarde() {
    Arguments arguments = new Arguments();
    String[]  args      = {DELETE + "=" + TRUE};

    assertTrue(arguments.setArguments(args));
    assertTrue(arguments.hasArgument(DELETE));
    assertEquals(TRUE, arguments.getArgument(DELETE));
  }

  @Test
  public void testEnkelMinZonderWaarde() {
    Arguments arguments = new Arguments();
    String[]  args      = {MINDELETE};

    assertTrue(arguments.setArguments(args));
    assertTrue(arguments.hasArgument(DELETE));
    assertEquals("", arguments.getArgument(DELETE));
  }

  @Test
  public void testEnkelMinMetWaarde1() {
    Arguments arguments = new Arguments();
    String[]  args      = {MINDELETE + "=" + TRUE};

    assertTrue(arguments.setArguments(args));
    assertTrue(arguments.hasArgument(DELETE));
    assertEquals(TRUE, arguments.getArgument(DELETE));
  }

  @Test
  public void testEnkelMinMetWaarde2() {
    Arguments arguments = new Arguments();
    String[]  args      = {MINDELETE, TRUE};

    assertTrue(arguments.setArguments(args));
    assertTrue(arguments.hasArgument(DELETE));
    assertEquals(TRUE, arguments.getArgument(DELETE));
  }

  @Test
  public void testDubbelMinZonderWaarde() {
    Arguments arguments = new Arguments();
    String[]  args      = {MINMINDELETE};

    assertTrue(arguments.setArguments(args));
    assertTrue(arguments.hasArgument(DELETE));
    assertEquals("", arguments.getArgument(DELETE));
  }

  @Test
  public void testDubbelMinMetWaarde1() {
    Arguments arguments = new Arguments();
    String[]  args      = {MINMINDELETE + "=" + TRUE};

    assertTrue(arguments.setArguments(args));
    assertTrue(arguments.hasArgument(DELETE));
    assertEquals(TRUE, arguments.getArgument(DELETE));
  }

  @Test
  public void testDubbelMinMetWaarde2() {
    Arguments arguments = new Arguments();
    String[]  args      = {MINMINDELETE, TRUE};

    assertTrue(arguments.setArguments(args));
    assertTrue(arguments.hasArgument(DELETE));
    assertEquals(TRUE, arguments.getArgument(DELETE));
  }

  @Test
  public void testTweeParametersZonderWaarde() {
    Arguments arguments = new Arguments();
    String[]  args      = {MINDELETE, MININSERT};

    assertTrue(arguments.setArguments(args));
    assertTrue(arguments.hasArgument(DELETE));
    assertTrue(arguments.hasArgument(INSERT));
  }

  @Test
  public void testTweeParametersMetWaarde() {
    Arguments arguments = new Arguments();
    String[]  args      = {MINDELETE, TRUE, MININSERT, TRUE};

    assertTrue(arguments.setArguments(args));
    assertTrue(arguments.hasArgument(DELETE));
    assertEquals(TRUE, arguments.getArgument(DELETE));
    assertTrue(arguments.hasArgument(INSERT));
    assertEquals(TRUE, arguments.getArgument(INSERT));
  }

  @Test
  public void testTweeParametersZonderMetWaarde() {
    Arguments arguments = new Arguments();
    String[]  args      = {MINDELETE, MININSERT, TRUE};

    assertTrue(arguments.setArguments(args));
    assertTrue(arguments.hasArgument(DELETE));
    assertTrue(arguments.hasArgument(INSERT));
    assertEquals(TRUE, arguments.getArgument(INSERT));
  }

  @Test
  public void testTweeParametersMetZonderWaarde() {
    Arguments arguments = new Arguments();
    String[]  args      = {MINDELETE, TRUE, MININSERT};

    assertTrue(arguments.setArguments(args));
    assertTrue(arguments.hasArgument(DELETE));
    assertEquals(TRUE, arguments.getArgument(DELETE));
    assertTrue(arguments.hasArgument(INSERT));
  }

  @Test
  public void testParameters() {
    Arguments arguments = new Arguments();
    String[]  args      = {MINDELETE, TRUE, MINMININSERT + "=" + FALSE,
                           UPDATE + "=" + TRUE};

    assertTrue(arguments.setArguments(args));
    arguments.setParameters(new String[]{DELETE});
    assertFalse(arguments.isValid());
    arguments.setParameters(new String[]{DELETE, INSERT, UPDATE});
    assertTrue(arguments.isValid());
  }

  @Test
  public void testVerplicht() {
    Arguments arguments = new Arguments();
    String[]  args      = {MINDELETE, TRUE, MINMININSERT + "=" + FALSE,
                           UPDATE + "=" + TRUE};

    assertTrue(arguments.setArguments(args));
    arguments.setVerplicht(new String[]{DELETE});
    assertTrue(arguments.isValid());
    arguments.setVerplicht(new String[]{INSERT});
    assertTrue(arguments.isValid());
    arguments.setVerplicht(new String[]{UPDATE});
    assertTrue(arguments.isValid());
  }
}
