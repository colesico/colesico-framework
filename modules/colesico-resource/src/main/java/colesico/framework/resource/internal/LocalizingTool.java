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

import colesico.framework.profile.*;
import colesico.framework.resource.ResourceException;
import colesico.framework.resource.ResourceKit;
import colesico.framework.resource.assist.FileParser;
import colesico.framework.resource.assist.PathTrie;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.function.Supplier;

@Singleton
public class LocalizingTool {

    private final Provider<Profile> profileProv;

    private final PathTrie<Localizer> pathTrie = new PathTrie<>("/");
    private final QualifierStandard qualifierStandard;

    public LocalizingTool(Provider<Profile> profileProv, ProfileConfigPrototype config) {
        this.profileProv = profileProv;
        this.qualifierStandard = config.getQualifierStandard();
    }

    /**
     * Register localizing qualifiers for specific resource path
     *
     * @param path
     * @param qualifiersSpec qualifier values set specification string in the format: qualifierName1=value1;qualifierName2=value2...
     *                       Qualifier values order is unimportant, it will be ordered at parsing
     */
    public void addLocalization(String path, String... qualifiersSpec) {
        final PathTrie.Node<Localizer> node = pathTrie.add(path);
        Localizer localizer = node.getValue();
        if (localizer == null) {
            localizer = new Localizer();
            node.setValue(localizer);
        }
        for (String qualifiersSpecItem : qualifiersSpec) {
            localizer.addLocalization(SubjectQualifiers.fromSpec(qualifiersSpecItem, qualifierStandard));
        }
    }

    /**
     * Localize based on current profile
     */
    public String localize(final String resourcePath, final ResourceKit.L10NMode mode) {
        return localize(resourcePath, mode, () -> {
            Profile profile = profileProv.get();
            return profile.getQualifiers();
        });
    }

    /**
     * Localize based on given qualifiers
     */
    public String localize(final String resourcePath, final ResourceKit.L10NMode mode, final ObjectiveQualifiers qualifiers) {
        return localize(resourcePath, mode, () -> qualifiers);
    }

    private String localize(final String resourcePath, final ResourceKit.L10NMode mode, final Supplier<ObjectiveQualifiers> qualifiersSup) {
        if (mode.equals(ResourceKit.L10NMode.NONE)) {
            return resourcePath;
        }

        Localizer localizer = pathTrie.find(resourcePath);
        if (localizer == null) {
            return resourcePath;
        }

        SubjectQualifiers qualifiers = localizer.localize(qualifiersSup.get());

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
    private String localizeFile(String resourcePath, SubjectQualifiers qualifiers) {
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

        sb.append(qualifiers.toSuffix('_'));

        if (!"".equals(fileExt)) {
            sb.append('.').append(fileExt);
        }
        return sb.toString();
    }

    private String localizeDir(String resourcePath, SubjectQualifiers qualifiers) {
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

        sb.append(qualifiers.toSuffix('_'));

        sb.append("/").append(fileName);
        if (!"".equals(fileExt)) {
            sb.append('.').append(fileExt);
        }
        return sb.toString();
    }

}


