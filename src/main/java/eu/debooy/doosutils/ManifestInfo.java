/**
 * Copyright 2010 Marco de Booij
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.Manifest;


/**
 * @author Marco de Booij
 */
public class ManifestInfo {
  private static final String  DATE_UNKNOWN     = "UNKNOWN";
  private static final String  LIB              = "/WEB-INF/lib";
  private static final String  MANIFEST         = "/META-INF/MANIFEST.MF";
  private static final String  VERSION_UNSTABLE = "UNSTABLE";

  private static  String    buildVersion;
  private static  String    buildDate;
  private static  Manifest  manif         = null;

  static {
    buildDetails();
  }

  private static void buildDetails() {
    try {
      URL manifestUrl;
      var classContainer  = ManifestInfo.class.getProtectionDomain()
                                        .getCodeSource()
                                        .getLocation().toString();

      if (classContainer.contains(LIB)) {
        classContainer  =
          classContainer.substring(0, classContainer.indexOf(LIB));
        manifestUrl     =
          new URL((new StringBuilder()).append(classContainer).append(MANIFEST)
                                       .toString());
      } else {
        manifestUrl     =
          new URL((new StringBuilder()).append("jar:").append(classContainer)
                                       .append("!").append(MANIFEST)
                                       .toString());

      }
      getManifest(manifestUrl);
      while (null == manif
          && classContainer.contains(LIB)) {
        classContainer  =
          classContainer.substring(0, classContainer.indexOf(LIB));
        manifestUrl     =
          new URL((new StringBuilder()).append(classContainer).append(MANIFEST)
                                       .toString());
        getManifest(manifestUrl);
      }

      if (null != manif) {
        var attr  = manif.getMainAttributes();
        buildVersion  = attr.getValue("Implementation-Version");
        buildDate     = attr.getValue("Build-Time");
      }
    } catch (MalformedURLException e) {
      buildVersion  = VERSION_UNSTABLE;
      buildDate     = DATE_UNKNOWN;
    }
  }

  public String getBuildVersion() {
    return DoosUtils.nullToValue(buildVersion, VERSION_UNSTABLE);
  }

  public String getBuildDate() {
    return DoosUtils.nullToValue(buildDate, DATE_UNKNOWN);
  }

  private static void getManifest(URL manifestUrl) {
    try {
      manif = new Manifest(manifestUrl.openStream());
    } catch (IOException e) {
      buildVersion  = VERSION_UNSTABLE;
      buildDate     = DATE_UNKNOWN;
    }
  }
}
