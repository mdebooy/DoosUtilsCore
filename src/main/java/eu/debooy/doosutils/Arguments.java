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

  private String getDashArgument(String arg) {
    if (arg.startsWith("--")) {
      return arg.substring(2);
    }

    return arg.substring(1);
  }

  public boolean hasArgument(String argument) {
    return args.containsKey(argument);
  }

  public boolean isValid() {
    var juist     = true;
    var volledig  = true;

    // Alle verplichte parameters aanwezig?
    for (var key: verplicht) {
      if (!args.containsKey(key)) {
        volledig  = false;
      }
    }

    // Enkel juiste parameters aanwezig?
    if (!parameters.isEmpty()) {
      for (var parameter: args.keySet()) {
        if (!parameters.contains(parameter)) {
          juist = false;
        }
      }
    }

    return juist && valid && volledig;
  }

  public boolean setArguments(String[] args){
    var     i     = 0;
    String  key;
    String  value;

    while (i < args.length) {
      if (args[i].startsWith("-")) {
        var argument  = getDashArgument(args[i]);
        var gelijk    = argument.indexOf('=');
        if (gelijk >= 1) {
          key   = argument.substring(0, gelijk);
          value = argument.substring(gelijk+1);
        } else {
          key   = argument;
          value = "";
          if (i+1 < args.length
              && !args[i+1].startsWith("-")) {
            i++;
            value = args[i];
          }
        }
        this.args.put(key, value);
      } else {
        setArg(args[i]);
      }
      i++;
    }

    return valid;
  }

  private void setArg(String arg) {
    var gelijk  = arg.indexOf('=');
    if (gelijk < 1) {
      valid = false;
    } else {
      args.put(arg.substring(0, gelijk),
               arg.substring(gelijk+1));
    }
  }

  public void setParameters(String[] parameters) {
    this.parameters = Arrays.asList(parameters);
  }

  public void setVerplicht(String[] verplicht) {
    this.verplicht  = Arrays.asList(verplicht);
  }

  @Override
  public String toString() {
    var result  = new StringBuilder();
    result.append("Arguments:");
    args.entrySet().forEach(entry ->
      result.append(entry.getKey()).append("=").append(entry.getValue())
            .append("|"));
    result.append("Verplicht:");
    verplicht.forEach(key -> result.append(key).append("|"));

    return result.toString();
  }
}
