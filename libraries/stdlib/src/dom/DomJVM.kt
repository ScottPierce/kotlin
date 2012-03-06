/**
 * JVM specific API implementations using JAXB and so forth which would not be used when compiling to JS
 */
package kotlin.dom

import org.w3c.dom.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.Source
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

import java.io.StringWriter
import javax.xml.transform.OutputKeys
import java.lang.Iterable
import java.util.List
import java.util.Collection

/** Creates a new document with the given document builder*/
fun createDocument(builder: DocumentBuilder): Document {
    return builder.newDocument().sure()
}

/** Creates a new document with an optional DocumentBuilderFactory */
fun createDocument(builderFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance().sure()): Document {
    return createDocument(builderFactory.newDocumentBuilder().sure())
}

/** Creates a new TrAX transformer */
fun createTransformer(source: Source? = null, factory: TransformerFactory = TransformerFactory.newInstance().sure()): Transformer {
    val transformer = if (source != null) {
        factory.newTransformer(source)
    } else {
        factory.newTransformer()
    }
    return transformer.sure()
}

/** Converts the node to an XML String */
fun Node.toXmlString(xmlDeclaration: Boolean = this is Document): String {
    return nodeToXmlString(this, xmlDeclaration)
}

/** Converts the collection of nodes to an XML String */
fun java.lang.Iterable<Node>.toXmlString(xmlDeclaration: Boolean = false): String {
    // TODO this should work...
    // return this.map<Node,String>{it.toXmlString()}.join("")
    val builder = StringBuilder()
    for (n in this) {
        builder.append(n.toXmlString(xmlDeclaration))
    }
    return builder.toString().sure()
}


/*
fun Document.toXmlString(xmlDeclaration: Boolean = true): String {
    return nodeToXmlString(this, xmlDeclaration)
}
*/

/** Converts the node to an XML String */
fun nodeToXmlString(node: Node, xmlDeclaration: Boolean): String {
    val transformer = createTransformer()
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, if (xmlDeclaration) "no" else "yes")
    val buffer = StringWriter()
    transformer.transform(DOMSource(node), StreamResult(buffer))
    return buffer.toString().sure()

}

