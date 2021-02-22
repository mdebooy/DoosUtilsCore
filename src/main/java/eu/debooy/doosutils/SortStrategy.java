/**
 * Copyright (c) 2011 Marco de Booij
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


/**
 * @author Marco de Booij
 *
 * @deprecated Gebruik de standaard Java <pre>Comparator<?></pre> om een
 *             afwijkende sortering te krijgen.
 */
@Deprecated(forRemoval=false, since = "2.4.0")
public interface SortStrategy {
  int sortingAlgorithm(Object obj1, Object obj2);
}
