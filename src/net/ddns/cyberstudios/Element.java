package net.ddns.cyberstudios;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

//TODO
// add methods to check ID, hint, check question type, check type, etc

public class Element {

    public static Map.Entry<String, String> EmptyEntry = new Map.Entry<String, String>() {
        @Override
        public String getKey() {
            return "";
        }

        @Override
        public String getValue() {
            return "";
        }

        @Override
        public String setValue(String value) {
            return "";
        }
    };

    private Map<String, String> attributes = new HashMap<>();
    private LinkedList<Element> children = new LinkedList<>();
    private Element parent;
    private String tag = "",
            id = "",
            hint = "",
            type = "",
            value = "";

    public Element() {
    }

    public Element(String tag) {
        this.tag = tag;
    }

    public Element(String tag, Element parent) {
        this.tag = tag;
        this.parent = parent;
    }

    public Element(String tag, Element parent, LinkedList<Element> children) {
        this.tag = tag;
        this.children = children;
        this.parent = parent;
    }

    public Element(String tag, Element parent, LinkedList<Element> children, Map<String, String> attributes) {
        this.tag = tag;
        this.attributes = attributes;
        this.children = children;
        this.parent = parent;

        processAttributes();
    }

    public Element(String tag, Element parent, Map<String, String> attributes) {
        this.tag = tag;
        this.attributes = attributes;
        this.parent = parent;

        processAttributes();
    }

    private void processAttributes() {
        id = attributes.entrySet().stream().filter(s -> s.getValue().equals("id")).findFirst().orElse(EmptyEntry).getValue();
        hint = attributes.entrySet().stream().filter(s -> s.getValue().equals("hint")).findFirst().orElse(EmptyEntry).getValue();
        type = attributes.entrySet().stream().filter(s -> s.getValue().equals("type")).findFirst().orElse(EmptyEntry).getValue();
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean hasParent(){
        return parent!=null;
    }

    public boolean hasId(){
        return !id.isEmpty();
    }

    public boolean hasHint(){
        return !hint.isEmpty();
    }

    public String getHint() {
        return hint;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void addAttribute(String attrKey, String attrValue) {
        this.attributes.put(attrKey, attrValue);
    }

    public void addChild(Element child) {
        this.children.add(child);
    }

    public LinkedList<Element> getChildren() {
        return children;
    }

    public Element getParent() {
        return parent;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

    public String getAttributesAsString() {
        return attributes.entrySet().stream().map(attrEntry -> " " + attrEntry.getKey() + "=" + attrEntry.getValue()).reduce(String::concat).orElse("");
    }

    public String getPreTag(String tag){
        String att = attributes.entrySet().stream().map(attrEntry -> " " + attrEntry.getKey() + "=" + attrEntry.getValue()).reduce(String::concat).orElse("");
        return "<" + tag + " class=\"" + this.tag + "\" " + att + ">";
    }

    public String getPostTag(String tag){
        return "</" + tag + ">";
    }

    public String getChildrenContent(){
        return children.stream().map(Element::toString).reduce(String::concat).orElse("");
    }

    public boolean isLeaf() {
        return !value.isEmpty();
    }

    public ContentConsumer toHtml(String htmlTag){
        return text -> getPreTag(htmlTag) + text + getPostTag(htmlTag);
    }

    @Override
    public String toString() {
        //TODO Check this
        return getPreTag(tag) + getChildrenContent() + getPostTag(tag);
    }
}
