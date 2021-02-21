/**
 * Copyright (c) 2009 Marco de Booij
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Marco de Booij
 */
public final class Arguments {
  private boolean             valid       = true;
  private Map<String, String> args        = new HashMap<>();
  private List<String>        parameters  = new ArrayList<>();
  private List<String>        verplicht   = new ArrayList<>();

  public Arguments() {
  }

  public Arguments(String[] args) {
    setArguments(Arrays.copyOf(args, args.length));
  }

  public String getArgument(String argument) {
    if (args.containsKey(argument)) {
      return args.get(argument);
    }

    return null;
  }

  public boolean hasArgument(String argument) {
    return args.containsKey(argument);
  }

  public boolean isValid() {
    boolean juist     = true;
    boolean volledig  = true;

    // Alle verplichte parameters aanwezig?
    for (String key: verplicht) {
      if (!args.containsKey(key)) {
        volledig  = false;
      }
    }

    // Enkel juiste parameters aanwezig?
    if (!parameters.isEmpty()) {
      for (String parameter: args.keySet()) {
        if (!parameters.contains(parameter)) {
          juist = false;
        }
      }
    }

    return juist && valid && volledig;
  }

  public boolean setArguments(String[] args){
    String  key;
    String  value;

    for (int i = 0; i < args.length; i++) {
      if (args[i].startsWith("-")) {
        String  argument;
        if (args[i].startsWith("--")) {
          argument  = args[i].substring(2);
        } else {
          argument  = args[i].substring(1);
        }
        if (argument.indexOf('=') > 0) {
          key   = argument.substring(0, argument.indexOf('='));
          value = argument.substring(argument.indexOf('=')+1);
        } else {
          key   = argument;
          if (i+1 < args.length
              && !args[i+1].startsWith("-")) {
            i++;
            value = args[i];
          } else {
            value = "";
          }
        }
        this.args.put(key, value);
      } else {
        if (args[i].indexOf('=') > 0) {
          key   = args[i].substring(0, args[i].indexOf('='));
          value = args[i].substring(args[i].indexOf('=')+1);
          this.args.put(key, value);
        } else {
          valid = false;
        }
      }
    }

    return valid;
  }

  public void setParameters(String[] parameters) {
    this.parameters = Arrays.asList(parameters);
  }

  public void setVerplicht(String[] verplicht) {
    this.verplicht  = Arrays.asList(verplicht);
  }

  @Override
  public String toString() {
    StringBuilder result  = new StringBuilder();
    result.append("Arguments:");
    args.entrySet().forEach(entry -> {
      result.append(entry.getKey()).append("=").append(entry.getValue())
            .append("|");
    });
    result.append("Verplicht:");
    verplicht.forEach(key -> {
      result.append(key).append("|");
    });

    return result.toString();
  }
}
