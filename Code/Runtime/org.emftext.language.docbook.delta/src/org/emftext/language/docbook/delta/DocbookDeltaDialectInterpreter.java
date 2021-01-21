package org.emftext.language.docbook.delta;

import org.deltaecore.core.variant.interpretation.locking.DEModelWriter;
import org.emftext.language.docbook.BookNode;
import org.emftext.language.docbook.ChapterNode;
import org.emftext.language.docbook.DocBookPackage;
import org.emftext.language.docbook.Node;
import org.emftext.language.docbook.ParagraphNode;
import org.emftext.language.docbook.ParagraphParentNode;
import org.emftext.language.docbook.SectionNode;
import org.emftext.language.docbook.resource.xml.util.XmlResourceUtil;

//This class is generated only once and will NOT be overwritten. Changed abstract methods of the base class have to be implemented manually.
public class DocbookDeltaDialectInterpreter extends DocbookAbstractDeltaDialectInterpreter {

	@Override
	protected boolean interpretCreateChapter(DEModelWriter modelWriter, String id, String title, String content, BookNode parent) {
		ChapterNode chapterNode = (ChapterNode) XmlResourceUtil.getResourceContent(content, DocBookPackage.eINSTANCE.getChapterNode());
		
		chapterNode.setId(id);
		chapterNode.setTitle(title);
		
		return interpretAddChapter(modelWriter, chapterNode, parent);
	}

	@Override
	protected boolean interpretCreateSection(DEModelWriter modelWriter, String id, String title, String content, ChapterNode parent) {
		SectionNode sectionNode = (SectionNode) XmlResourceUtil.getResourceContent(content, DocBookPackage.eINSTANCE.getSectionNode());
		
		sectionNode.setId(id);
		sectionNode.setTitle(title);
		
		return interpretAddSection(modelWriter, sectionNode, parent);
	}

	@Override
	protected boolean interpretCreateParagraph(DEModelWriter modelWriter, String id, String content, ParagraphParentNode parent) {
		ParagraphNode paragraphNode = (ParagraphNode) XmlResourceUtil.getResourceContent(content, DocBookPackage.eINSTANCE.getParagraphNode());
		
		paragraphNode.setId(id);
		
		return interpretAddParagraph(modelWriter, paragraphNode, parent);
	}

	@Override
	protected boolean interpretAddParagraph(DEModelWriter modelWriter, ParagraphNode paragraph, ParagraphParentNode parent) {
		parent.addParagraphNode(paragraph);
		return true;
	}

	@Override
	protected boolean interpretChangeId(DEModelWriter modelWriter, String id, Node node) {
		node.setId(id);
		return true;
	}
}