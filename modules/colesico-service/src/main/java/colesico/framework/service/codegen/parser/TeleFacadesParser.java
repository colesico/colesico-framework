/*
 * Copyright 20014-2018 Vladlen Larionov
 *             and others as noted
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
 *
 */

package colesico.framework.service.codegen.parser;

import colesico.framework.assist.codegen.CodegenException;
import colesico.framework.assist.codegen.CodegenUtils;
import colesico.framework.service.Compound;
import colesico.framework.service.TeleMethodName;
import colesico.framework.service.TeleView;
import colesico.framework.service.codegen.model.*;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.*;

public final class TeleFacadesParser {

    private final ProcessorContext context;

    public TeleFacadesParser(ProcessorContext context) {
        this.context = context;
    }

    protected TeleVarElement createTeleVarElement(TeleMethodElement teleMethod, TeleVarElement parentTeleVar, Deque<VariableElement> varStack) {

        VariableElement var = varStack.peek(); // get stack head element (first element of dequeq)

        //================= Check composition or parameter
        Compound compounddAnn = var.getAnnotation(Compound.class);
        if (compounddAnn == null) {
            TeleParamElement teleParam = new TeleParamElement(var);
            teleMethod.linkVariable(teleParam);
            teleParam.setParentVariable(parentTeleVar);
            context.getModulatorKit().notifyLinkTeleParam(teleParam, varStack);
            return teleParam;
        }

        //=================== Create composition


        // Check recursive objects
        VariableElement methodParam = varStack.peekLast(); // get element from stack tail  (last element of deque)
        varStack.pop();     // temporaty remove var from stack head   (first element of deque)
        for (VariableElement ve : varStack) {
            if (ve.asType().toString().equals(var.asType().toString())) {
                TeleFacadeElement teleFacade = teleMethod.getParentTeleFacade();
                throw CodegenException.of().message("Recursive composition for: " +
                        var.asType().toString() + "->" + var.getSimpleName().toString() +
                        " in: " +
                        teleFacade.getParentService().getOriginClass().asType().toString() +
                        "->" + teleMethod.getProxyMethod().getName() + "(..." +
                        methodParam.toString()
                        + "...) ")
                        .element(methodParam)
                        .create();
            }
        }
        varStack.push(var); // push var back to stack head

        TeleCompElement teleComp = new TeleCompElement(var);
        teleMethod.linkVariable(teleComp);

        // Check that scope of composition is acceptable for composition
        TypeElement paramTypeElm = context.getElementUtils().getTypeElement(var.asType().toString());
        if (paramTypeElm == null) {
            throw CodegenException.of().message("Tele-reader does not support variable/parameter scope:"
                    + var.getSimpleName().toString()
                    + " of scope "
                    + var.asType().toString())
                    .element(var)
                    .create();
        }

        // ============ Process fields

        // Retrieve class fields for composition
        List<? extends Element> members = context.getElementUtils().getAllMembers(paramTypeElm);

        // Filter fields to get only acceptable
        List<VariableElement> fields = new ArrayList<>();
        for (Element e : members) {
            if (ElementKind.FIELD.equals(e.getKind()) &&
                    !(e.getModifiers().contains(Modifier.FINAL) || e.getModifiers().contains(Modifier.STATIC))
                    && e.asType().getKind().equals(TypeKind.DECLARED)) {
                fields.add(VariableElement.class.cast(e));
            }
        }

        // Process fields
        Set<String> masterViews = getTeleViewKeys(varStack.getFirst().getAnnotation(TeleView.class));
        for (VariableElement field : fields) {
            // Check field by "TeleView"
            Set<String> fieldViews = getTeleViewKeys(field.getAnnotation(TeleView.class));
            if (!inTeleView(masterViews, fieldViews)) {
                continue;
            }

            varStack.push(field);
            TeleVarElement teleVar = createTeleVarElement(teleMethod, teleComp, varStack);
            varStack.pop();
            teleComp.addVariable(teleVar);
        }

        return teleComp;
    }

    protected void addTeleMethodParams(TeleMethodElement teleMethod) {
        ExecutableElement method = teleMethod.getProxyMethod().getOriginMethod();
        for (VariableElement param : method.getParameters()) {
            Deque<VariableElement> varStack = new ArrayDeque<>();
            varStack.push(param);
            TeleVarElement teleParam = createTeleVarElement(teleMethod, null, varStack);
            teleMethod.addParameter(teleParam);
        }
    }

    protected void addTeleMethods(TeleFacadeElement teleFacade) {
        ServiceElement service = teleFacade.getParentService();
        for (ProxyMethodElement proxyMethod : service.getProxyMethods()) {
            if (proxyMethod.isLocal()) {
                continue;
            }
            TeleMethodName teleMethodNameAnn = proxyMethod.getOriginMethod().getAnnotation(TeleMethodName.class);
            final String teleMethodName;
            if (teleMethodNameAnn != null) {
                teleMethodName = teleMethodNameAnn.value();
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

    private Set<String> getTeleViewKeys(TeleView teleViewAnnotation) {
        Set<String> result = new HashSet<>();
        if (teleViewAnnotation == null) {
            return result;
        }
        TypeMirror[] keys = CodegenUtils.getAnnotationValueTypeMirrors(teleViewAnnotation, a -> a.value());
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
