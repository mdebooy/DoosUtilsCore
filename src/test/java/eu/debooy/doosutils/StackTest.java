/**
 * Copyright (c) 2016 Marco de Booy
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

import java.util.NoSuchElementException;
import static junit.framework.TestCase.assertEquals;
import org.junit.Test;


/**
 * @author Marco de Booij
 */
public class StackTest {
  @Test(expected = NoSuchElementException.class)
  public void stackLinkedListException1() {
    Stack<Integer>  stack = new StackLinkedList<>();
    stack.pop();
  }

  @Test(expected = NoSuchElementException.class)
  public void stackLinkedListException2() {
    Stack<Integer>  stack = new StackLinkedList<>();
    stack.push(1);
    stack.pop();
    stack.pop();
  }

  @Test
  public void stackLinkedList() {
    Stack<Integer>  stack = new StackLinkedList<>();
    stack.push(1);
    stack.push(2);
    stack.push(3);
    stack.push(3);
    stack.push(4);
    assertEquals(5, stack.size());
    assertEquals(4, stack.pop().intValue());
    assertEquals(3, stack.pop().intValue());
    assertEquals(3, stack.pop().intValue());
    assertEquals(2, stack.pop().intValue());
    assertEquals(1, stack.pop().intValue());
    assertEquals("", stack.toString());
    assertEquals(0 , stack.size());
  }
}
