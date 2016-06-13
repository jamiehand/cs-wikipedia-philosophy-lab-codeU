package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import java.lang.Character;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {

	final static WikiFetcher wf = new WikiFetcher();

	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 *
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 *
	 * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		/* stack to keep track of parentheses */
		Stack<Integer> parenStack = new Stack<Integer>();

        // some example code to get you started

		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";

		// parse into paragraphs
		Elements paragraphs = wf.fetchWikipedia(url);

		url = getFirstValidLink(paragraphs, url, parenStack);
		parenStack = new Stack<Integer>();

		// Element firstPara = paragraphs.get(0);
		//
		// Iterable<Node> iter = new WikiNodeIterable(firstPara);
		// for (Node node: iter) {
		// 	// System.out.println("\nprinting");
		// 	if (node instanceof TextNode) {
		// 		System.out.print(node);
		// 	}
    // }

    // the following throws an exception so the test fails
    // until you update the code
    String msg = "Complete this lab by adding your code and removing this statement.";
    throw new UnsupportedOperationException(msg);
	}

	static String getFirstValidLink(Elements paragraphs, String currentURL,
												Stack<Integer> parenStack) {
		for (Element para: paragraphs) {
			Iterable<Node> iter = new WikiNodeIterable(para);
			for (Node node: iter) {
				if (node instanceof TextNode) {
					checkForParens(node, parenStack);
					}

				/* verify it's a link */
				if (node.hasAttr("href")) {
					/* verify the parenStack is empty (i.e. we're not w/in parentheses) */
					if (parenStack.isEmpty()) {
						String newURL = node.attr("abs:href");
						/* verify it's not a link to the current page */
						if (!newURL.equals(currentURL)) {
							Node childNode = node.childNode(0);
							/* verify the node is text (not title) and lowercase */
							if (isTextAndLowerCase(childNode)) {
								/* verify it's not in italics */
								if (notInItalics(childNode)) {
									System.out.println(childNode.attr("text"));
									return newURL;
								}
							}
						}
					}
			  }  /* end link if statement */

			}
		}
		System.exit(1); // No valid link found.
		return "This should not be reached.";
	}


	static boolean notInItalics(Node node) {
		/* check all ancestors of node to see if they italicize their children */
		Node parent = node.parent();

		/* base case: reached root without finding 'i' or 'em' tag */
		if (node.parent() == null) {
			return true;
		}

		/* if this is an Element, check whether its tag is 'i' or 'em' */
		if (parent instanceof Element) {
			Element parentElement = (Element) parent;
			if (parentElement.tag().toString().equals("i") ||
					parentElement.tag().toString().equals("em")) {
				return false;
			}
		}

		/* keep checking up the ancestors */
		return notInItalics(node.parent());
	}

	static boolean isTextAndLowerCase(Node node) {
		/* make sure it's a node with text, rather than a title;
		 * nodes with "titles" contain information like "The time period
		 * mentioned near this tag is ambiguous. (December 2014)", not
		 * the link text that we want. */
		if (node.hasAttr("text")){
			Character firstChar = node.attr("text").charAt(0);
			if (Character.isLowerCase(firstChar)) {
				return true;
			}
		}
		return false;
	}

	static void checkForParens(Node node, Stack<Integer> parenStack) {
		/* push a '1' for new open parentheses; pop for new close parentheses */
		String nodeString = node.toString();
		for (int i=0; i<nodeString.length(); i++) {
			Character currentChar = nodeString.charAt(i);
			if (currentChar == '(') {
				parenStack.push(1);
			} else if (currentChar == ')') {
				parenStack.pop();
			}
		}
	}

}
