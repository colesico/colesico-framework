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

package colesico.framework.service.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.FrameworkAbstractParser;
import colesico.framework.assist.codegen.model.*;
import colesico.framework.service.Compound;
import colesico.framework.service.LocalParam;
import colesico.framework.service.TeleMethodName;
import colesico.framework.service.TeleView;
import colesico.framework.service.codegen.model.*;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.*;

public final class TeleFacadesParser extends FrameworkAbstractParser {

    private final ProcessorContext context;

    public TeleFacadesParser(ProcessorContext context) {
        super(context.getProcessingEnv());
        this.context = context;
    }

    protected TeleVarElement createTeleVarElement(TeleMethodElement teleMethod, TeleVarElement parentTeleVar, Deque<VarElement> varStack, Integer paramIndex) {

        VarElement var = varStack.peek(); // get stack head element (first element of Deque)

        if (var == null) {
            throw CodegenException.of()
                    .message("No variable")
                    .build();
        }

        AnnotationToolbox<LocalParam> localParamAnn = var.getAnnotation(LocalParam.class);

        //================= Check compound or parameter
        AnnotationToolbox<Compound> compounddAnn = var.getAnnotation(Compound.class);
        if (compounddAnn == null) {
            // simple parameter
            TeleParamElement teleParam = new TeleParamElement(var, localParamAnn != null, paramIndex);
            teleMethod.linkVariable(teleParam);
            teleParam.setParentVariable(parentTeleVar);
            context.getModulatorKit().notifyLinkTeleParam(teleParam, varStack);
            return teleParam;
        }

        //=================== Create compound


        // Check recursive objects
        VarElement methodParam = varStack.peekLast(); // get element from stack tail  (last element of dequeue)
        varStack.pop();     // temporaty remove var from stack head   (first element of deque)
        for (VarElement ve : varStack) {
            if (ve.asType().toString().equals(var.asType().toString())) {
                TeleFacadeElement teleFacade = teleMethod.getParentTeleFacade();
                throw CodegenException.of().message("Recursive composition for: " +
                        var.asType().toString() + "->" + var.getName() +
                        " in: " +
                        teleFacade.getParentService().getOriginClass().asType().toString() +
                        "->" + teleMethod.getProxyMethod().getName() + "(..." +
                        methodParam.toString()
                        + "...) ")
                        .element(methodParam)
                        .build();
            }
        }
        varStack.push(var); // push var back to stack head

        TeleCompElement teleComp = new TeleCompElement(var, localParamAnn != null);
        teleComp.setParentVariable(parentTeleVar);
        teleMethod.linkVariable(teleComp);

        // ============ Process fields

        // Retrieve class fields for composition
        // Filter fields to get only acceptable
        List<FieldElement> fields = var.asClassType().asClassElement().getFieldsFiltered(
                f -> !f.unwrap().getModifiers().contains(Modifier.FINAL)
                        && !f.unwrap().getModifiers().contains(Modifier.STATIC)
                        && f.unwrap().asType().getKind().equals(TypeKind.DECLARED)
        );

        // Process fields
        AnnotationToolbox<TeleView> teleViewAnn = varStack.getFirst().getAnnotation(TeleView.class);
        Set<String> masterViews = getTeleViewKeys(teleViewAnn);
        for (FieldElement field : fields) {
            // Check field by "TeleView"
            teleViewAnn = field.getAnnotation(TeleView.class);
            Set<String> fieldViews = getTeleViewKeys(teleViewAnn);
            if (!inTeleView(masterViews, fieldViews)) {
                continue;
            }

            if (field.asClassType() == null) {
                throw CodegenException.of().message("Unsupported field type kind for tele-view " + var.asClassType().asClassElement().getName() + "->" + field.getName())
                        .element(field.unwrap())
                        .build();
            }

            varStack.push(field);
            TeleVarElement teleVar = createTeleVarElement(teleMethod, teleComp, varStack, paramIndex);
            varStack.pop();
            teleComp.addVariable(teleVar);
        }

        return teleComp;
    }

    protected void addTeleMethodParams(TeleMethodElement teleMethod) {
        MethodElement method = teleMethod.getProxyMethod().getOriginMethod();
        int paramIndex = 0;
        for (ParameterElement param : method.getParameters()) {
            Deque<VarElement> varStack = new ArrayDeque<>();
            if (param.asClassType() == null) {
                throw CodegenException.of()
                        .message("Unsupported parameter type for tele-method "
                                + teleMethod.getParentTeleFacade().getParentService().getOriginClass().getName() + "."
                                + teleMethod.getName() + "(..." + param.asType().toString() + " " + param.getName() + "...)")
                        .element(param.unwrap())
                        .build();
            }
            varStack.push(param);
            TeleVarElement teleParam = createTeleVarElement(teleMethod, null, varStack, paramIndex);
            teleMethod.addParameter(teleParam);
            paramIndex++;
        }
    }

    protected void addTeleMethods(TeleFacadeElement teleFacade) {
        ServiceElement service = teleFacade.getParentService();
        for (ProxyMethodElement proxyMethod : service.getProxyMethods()) {
            if (proxyMethod.isLocal()) {
                continue;
            }
            AnnotationToolbox<TeleMethodName> teleMethodNameAnn = proxyMethod.getOriginMethod().getAnnotation(TeleMethodName.class);
            final String teleMethodName;
            if (teleMethodNameAnn != null) {
                teleMethodName = teleMethodNameAnn.unwrap().value();
            } else {
                teleMethodName = proxyMethod.getName();
            }
            TeleMethodElement teleMethod = new TeleMethodElement(teleMethodName, proxyMethod);
            teleFacade.addTeleMethod(teleMethod);
            context.getModulatorKit().notifyAddTeleMethod(teleMethod);
            addTeleMethodParams(teleMethod);
        }
    }

    public void parseTeleFacades(ServiceElement service) {
        for (TeleFacadeElement teleFacade : service.getTeleFacades()) {
            addTeleMethods(teleFacade);
            context.getModulatorKit().notifyTeleFacadeParsed(teleFacade);
        }
    }

    private Set<String> getTeleViewKeys(AnnotationToolbox<TeleView> teleViewAnnotation) {
        Set<String> result = new HashSet<>();
        if (teleViewAnnotation == null) {
            return result;
        }
        TypeMirror[] keys = teleViewAnnotation.getValueTypeMirrors(TeleView::value);
        for (TypeMirror k : keys) {
            result.add(k.toString());
        }
        return result;
    }


    private boolean inTeleView(Set<String> masterViews, Set<String> fieldViews) {
        if (masterViews.isEmpty()) {
            return true;
        }
        for (String mv : masterViews) {
            if (fieldViews.contains(mv)) {
                return true;
            }
        }
        return false;
    }
}
