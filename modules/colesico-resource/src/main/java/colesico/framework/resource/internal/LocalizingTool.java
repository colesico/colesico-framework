/*
 * Copyright © 2014-2020 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package colesico.framework.resource.internal;

import colesico.framework.profile.Localizer;
import colesico.framework.profile.Profile;
import colesico.framework.profile.ProfileConfigPrototype;
import colesico.framework.resource.ResourceException;
import colesico.framework.resource.ResourceKit;
import colesico.framework.resource.assist.FileParser;
import colesico.framework.resource.assist.PathTrie;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.function.Supplier;

@Singleton
public class LocalizingTool {

    private final Provider<Profile> profileProv;

    private final String[] qualifiersNames;
    private final PathTrie<Localizer> pathTrie = new PathTrie<>("/");

    public LocalizingTool(Provider<Profile> profileProv, ProfileConfigPrototype config) {
        this.profileProv = profileProv;
        this.qualifiersNames = config.getQualifiersNames();
    }

    /**
     * Register qualifiers values for  specific resource path
     * @param path
     * @param qualifiersSetSpec qualifier values set specification string in the format: qualifier1=value1;qualifier2=value2...
     */
    public void  addQualifiers(String path, String... qualifiersSetSpec) {
        final PathTrie.Node<Localizer> node = pathTrie.add(path);
        Localizer localizer = node.getValue();
        if (localizer == null) {
            localizer = new Localizer();
            node.setValue(localizer);
        }
        localizer.add(qualifiersNames, qualifiersSetSpec);
    }

    public String localize(final String resourcePath, final ResourceKit.L10NMode mode) {
        return localize(resourcePath, mode, () -> {
            Profile profile = profileProv.get();
            return profile.getQualifiers();
        });
    }

    public String localize(final String resourcePath, final ResourceKit.L10NMode mode, final String[] qualifiers) {
        return localize(resourcePath, mode, () -> qualifiers);
    }

    private String localize(final String resourcePath, final ResourceKit.L10NMode mode, final Supplier<String[]> qualifiersSup) {
        if (mode.equals(ResourceKit.L10NMode.NONE)) {
            return resourcePath;
        }

        Localizer localizer = pathTrie.find(resourcePath);
        if (localizer == null) {
            return resourcePath;
        }

        String[] qualifiers = localizer.localize(qualifiersSup.get());

        if (qualifiers == null) {
            return resourcePath;
        }

        switch (mode) {
            case FILE:
                return localizeFile(resourcePath, qualifiers);
            case DIR:
                return localizeDir(resourcePath, qualifiers);
            default:
                throw new ResourceException("Unsupported localization mode: " + mode);
        }
    }


    /**
     * @param resourcePath
     * @param qualifiers
     * @return
     */
    private String localizeFile(String resourcePath, String[] qualifiers) {
        /*
              valid:

              root/dir/file.txt - > root/dir/file_en_GB.txt
              root/dir/file - > root/dir/file_en_GB
              root/dir -> root/dir_en_GB
              file.txt -> file_en_GB.txt
              file -> file_en_GB

              invalid:

              root/dir/ - > root/dir/_en_GB !
              .txt - > .txt_en_GB !
              /.txt - > /.txt_en_GB !
              / -> /_en_GB !

         */
        final FileParser fp = new FileParser(resourcePath);
        final String path = fp.path();
        final String fileName = fp.fileName();

        if ("".equals(fileName)) {
            throw new ResourceException("Empty file name: " + resourcePath);
        }

        final String fileExt = fp.extension();

        final StringBuilder sb;
        if ("".equals(path)) {
            sb = new StringBuilder(fileName);
        } else {
            sb = new StringBuilder(path).append("/").append(fileName);
        }

        for (String qualifier : qualifiers) {
            sb.append('_').append(qualifier);
        }

        if (!"".equals(fileExt)) {
            sb.append('.').append(fileExt);
        }
        return sb.toString();
    }

    private String localizeDir(String resourcePath, String[] qualifiers) {
        /*
              root/dir/ - > root/dir_en_GB/
              root/dir  - > root_en_GB/dir
              root/dir/file - > root/dir_en_GB/file
              root/dir/file.txt - > root/dir_en_GB/file.txt
              file.txt - > _en_GB/file.txt
              .txt - > _en_GB/.txt
              /.txt - > _en_GB/.txt
              / -> _en_GB/

         */
        final FileParser fp = new FileParser(resourcePath);
        final String path = fp.path();
        final String fileName = fp.fileName();
        final String fileExt = fp.extension();

        final StringBuilder sb;

        sb = new StringBuilder(path);
        for (String qualifier : qualifiers) {
            String qvalue = StringUtils.lowerCase(qualifier);
            sb.append('_').append(qvalue);
        }
        sb.append("/").append(fileName);
        if (!"".equals(fileExt)) {
            sb.append('.').append(fileExt);
        }
        return sb.toString();
    }

}


