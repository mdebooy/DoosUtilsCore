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
 * @author Marco de Booij (booymaa)
 */
public class QueueTest {
  @Test(expected = NoSuchElementException.class)
  public void queueLinkedListException1() {
    Queue<Integer> queue  = new QueueLinkedList<>();
    queue.dequeue();
  }

  @Test(expected = NoSuchElementException.class)
  public void queueLinkedListException2() {
    Queue<Integer> queue  = new QueueLinkedList<>();
    queue.enqueue(1);
    queue.dequeue();
    queue.dequeue();
  }

  @Test
  public void queueLinkedList() {
    Queue<Integer> queue  = new QueueLinkedList<>();
    queue.enqueue(1);
    queue.enqueue(2);
    queue.enqueue(3);
    queue.enqueue(3);
    queue.enqueue(4);
    assertEquals(5, queue.size());
    assertEquals(1, queue.dequeue().intValue());
    assertEquals(2, queue.dequeue().intValue());
    assertEquals(3, queue.dequeue().intValue());
    assertEquals(3, queue.dequeue().intValue());
    assertEquals(4, queue.dequeue().intValue());
    assertEquals("", queue.toString());
    assertEquals(0, queue.size());
  }
}
