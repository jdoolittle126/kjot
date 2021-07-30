package com.github.jdoolittle126.kjot.handlers

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.text.CharArrayUtil
import org.jetbrains.kotlin.idea.kdoc.KDocElementFactory
import org.jetbrains.kotlin.kdoc.psi.api.KDoc
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtFunction
import org.jetbrains.kotlin.psi.KtProperty

class KjotEnterHandler : EnterHandlerDelegateAdapter() {

    override fun postProcessEnter(file: PsiFile, editor: Editor, dataContext: DataContext): EnterHandlerDelegate.Result {
        if (file is KtFile && isInKDoc(editor, editor.caretModel.offset)) {
            val project: Project = file.getProject()
            val documentManager = PsiDocumentManager.getInstance(project)
            documentManager.commitAllDocuments()
            val elementAtCaret = file.findElementAt(editor.caretModel.offset)

            val kdoc = PsiTreeUtil.getParentOfType(elementAtCaret, KDoc::class.java)
                ?: return EnterHandlerDelegate.Result.Continue

            println("\n" + kdoc.getOwner() + "\n")
            val owner = kdoc.getOwner();
            when(owner) {
                is KtClass -> {
                    println("Comment for class\n\tPROPS")
                    for(y in owner.getProperties()) {
                        println(y)
                    }
                    //println("\n\tKEYWORD\n" + owner.getDeclarationKeyword().toString())
                    val meeper = KDocElementFactory(project)
                    val honk = meeper.createNameFromText("WOOOOW")
                    val vroom = meeper.createKDocFromText("/** this is my kdoc wow cool! */")
                    println("doing good")
                    kdoc.replace(vroom)
                }
                is KtFunction -> println("Comment for function")
                is KtProperty -> println("Comment for property")
            }

            WriteCommandAction.runWriteCommandAction(project) {
                editor.document.insertString(editor.caretModel.offset, "WOOOW")
            };

            //val test = KDocElementFactory(project)
            //println(test.toString())

            //println("we did it" + doc.toString() )

            //PsiTreeUtil.findChildOfType(KtPsiFactory(project).createDeclaration<KtFunction>("Test"), KDoc::class.java)

            //ApplicationManager.getApplication().runWriteAction {
            //KDocElementFactory
            //CodeStyleManager.getInstance(project).reformat(it)
            //}



        }

        return EnterHandlerDelegate.Result.Continue
    }

    private fun isKotlin(): Boolean {
        return true;
    }

    private fun isInKDoc(editor: Editor, offset: Int): Boolean {
        val document = editor.document
        val docChars = document.charsSequence
        var i = CharArrayUtil.lastIndexOf(docChars, "/**", offset)
        if (i >= 0) {
            i = CharArrayUtil.indexOf(docChars, "*/", i)
            return i > offset
        }
        return false
    }

}