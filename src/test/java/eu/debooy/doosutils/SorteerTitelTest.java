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

import junit.framework.TestCase;
import org.junit.Test;


/**
 *
 * @author Marco de Booij
 */
public class SorteerTitelTest extends TestCase {
  @Test
  public void testDuits() {
    assertEquals("Frau", SorteerTitel.sorteerwaarde("die Frau", "de"));
    assertEquals("Frau", SorteerTitel.sorteerwaarde("eine Frau", "de"));
  }

  @Test
  public void testEngels() {
    assertEquals("woman", SorteerTitel.sorteerwaarde("a woman", "en"));
    assertEquals("woman", SorteerTitel.sorteerwaarde("the woman", "en"));
    assertEquals("artist", SorteerTitel.sorteerwaarde("an artist", "en"));
  }

  @Test
  public void testFrans() {
    assertEquals("femme", SorteerTitel.sorteerwaarde("la femme", "fr"));
    assertEquals("femme", SorteerTitel.sorteerwaarde("une femme", "fr"));
    assertEquals("artist", SorteerTitel.sorteerwaarde("l'artist", "fr"));
  }

  @Test
  public void testNederlands() {
    assertEquals("vrouw", SorteerTitel.sorteerwaarde("de vrouw", "nl"));
    assertEquals("vrouw", SorteerTitel.sorteerwaarde("een vrouw", "nl"));
    assertEquals("meisje", SorteerTitel.sorteerwaarde("het meisje", "nl"));
  }
}
