/**
 * Copyright (c) 2016 Marco de Booij
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

import java.util.NoSuchElementException;


/**
 * @author Marco de Booij (booymaa)
 */
public class QueueLinkedList<T> implements Queue<T> {
  private int   elementen;
  private Node  eerste;
  private Node  laatste;

  private class Node {
    private T     element;
    private Node  volgende;
  }

  @Override
  public T dequeue() {
    if (elementen == 0) {
      throw new NoSuchElementException();
    }

    T node  = eerste.element;
    eerste  = eerste.volgende;
    if (--elementen == 0) {
      laatste = null;
    }

    return node;
  }

  @Override
  public QueueLinkedList<T> enqueue(T element) {
    var node        = laatste;
    laatste         = new Node();
    laatste.element = element;

    if (elementen++ == 0) {
      eerste  = laatste;
    } else {
      node.volgende = laatste;
    }

    return this;
  }

  @Override
  public int size() {
    return elementen;
  }

  @Override
  public String toString() {
    var sb    = new StringBuilder();
    var node  = eerste;
    while (node != null) {
      sb.append(", ").append(node.element);
      node = node.volgende;
    }

    return sb.toString().replaceFirst(", ", "");
  }
}
