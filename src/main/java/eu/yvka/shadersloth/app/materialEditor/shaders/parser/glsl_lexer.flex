/* JFlex example: partial Java language lexer specification */
package eu.yvka.shadersloth.app.materialEditor.shaders.parser;

import java_cup.runtime.*;
import static eu.yvka.shadersloth.app.materialEditor.shaders.parser.Symbols.*;

/**
 * This class is a simple example lexer.
 */
%%

%class GLSLLexer
%unicode
%line
%column
%type Token

%{
  StringBuffer string = new StringBuffer();

  private Token token(int type) {
    return new Token(type, yyline, yycolumn);
  }
  private Token token(int type, String value) {
    return new Token(type, yyline, yycolumn, value);
  }
%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
// Comment can be the last line of the file, without line terminator.
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent       = ( [^*] | \*+ [^/*] )*

Identifier = [:jletter:] [:jletterdigit:]*

DecIntegerLiteral = 0 | [1-9][0-9]*

PreProcessorStatement = "#" .*

%state STRING

%%

/* keywords */
<YYINITIAL> {
    "attribute"               { return token(ATTRIBUTE); }
    "const"                   { return token(CONST); }
    "uniform"                 { return token(UNIFORM); }
    "varying"                 { return token(VARYING); }
    "buffer"                  { return token(BUFFER); }
    "shared"                  { return token(SHARED); }
    "coherent"                { return token(COHERENT); }
    "volatile"                { return token(VOLATILE); }
    "restrict"                { return token(RESTRICT); }
    "readonly"                { return token(READONLY); }
    "writeonly"               { return token(WRITEONLY); }
    "atomic_uint"             { return token(ATOMIC_UINT); }
    "layout"                  { return token(LAYOUT); }
    "centroid"                { return token(CENTROID); }
    "flat"                    { return token(FLAT); }
    "smooth"                  { return token(SMOOTH); }
    "noperspective"           { return token(NOPERSPECTIVE); }
    "patch"                   { return token(PATCH); }
    "sample"                  { return token(SAMPLE); }
    "break"                   { return token(BREAK); }
    "continue"                { return token(CONTINUE); }
    "do"                      { return token(DO); }
    "for"                     { return token(FOR); }
    "while"                   { return token(WHILE); }
    "switch"                  { return token(SWITCH); }
    "case"                    { return token(CASE); }
    "default"                 { return token(DEFAULT); }
    "if"                      { return token(IF); }
    "else"                    { return token(ELSE); }
    "subroutine"              { return token(SUBROUTINE); }
    "in"                      { return token(IN); }
    "out"                     { return token(OUT); }
    "inout"                   { return token(INOUT); }
    "float"                   { return token(FLOAT); }
    "double"                  { return token(DOUBLE); }
    "int"                     { return token(INT); }
    "void"                    { return token(VOID); }
    "bool"                    { return token(BOOL); }
    "true"                    { return token(TRUE); }
    "false"                   { return token(FALSE); }
    "invariant"               { return token(INVARIANT); }
    "precise"                 { return token(PRECISE); }
    "discard"                 { return token(DISCARD); }
    "return"                  { return token(RETURN); }
    "mat2"                    { return token(MAT2); }
    "mat3"                    { return token(MAT3); }
    "mat4"                    { return token(MAT4); }
    "dmat2"                   { return token(DMAT2); }
    "dmat3"                   { return token(DMAT3); }
    "dmat4"                   { return token(DMAT4); }
    "mat2x2"                  { return token(MAT2X2); }
    "mat2x3"                  { return token(MAT2X3); }
    "mat2x4"                  { return token(MAT2X4); }
    "dmat2x2"                 { return token(DMAT2X2); }
    "dmat2x3"                 { return token(DMAT2X3); }
    "dmat2x4"                 { return token(DMAT2X4); }
    "mat3x2"                  { return token(MAT3X2); }
    "mat3x3"                  { return token(MAT3X3); }
    "mat3x4"                  { return token(MAT3X4); }
    "dmat3x2"                 { return token(DMAT3X2); }
    "dmat3x3"                 { return token(DMAT3X3); }
    "dmat3x4"                 { return token(DMAT3X4); }
    "mat4x2"                  { return token(MAT4X2); }
    "mat4x3"                  { return token(MAT4X3); }
    "mat4x4"                  { return token(MAT4X4); }
    "dmat4x2"                 { return token(DMAT4X2); }
    "dmat4x3"                 { return token(DMAT4X3); }
    "dmat4x4"                 { return token(DMAT4X4); }
    "vec2"                    { return token(VEC2); }
    "vec3"                    { return token(VEC3); }
    "vec4"                    { return token(VEC4); }
    "ivec2"                   { return token(IVEC2); }
    "ivec3"                   { return token(IVEC3); }
    "ivec4"                   { return token(IVEC4); }
    "bvec2"                   { return token(BVEC2); }
    "bvec3"                   { return token(BVEC3); }
    "bvec4"                   { return token(BVEC4); }
    "dvec2"                   { return token(DVEC2); }
    "dvec3"                   { return token(DVEC3); }
    "dvec4"                   { return token(DVEC4); }
    "uint"                    { return token(UINT); }
    "uvec2"                   { return token(UVEC2); }
    "uvec3"                   { return token(UVEC3); }
    "uvec4"                   { return token(UVEC4); }
    "lowp"                    { return token(LOWP); }
    "mediump"                 { return token(MEDIUMP); }
    "highp"                   { return token(HIGHP); }
    "precision"               { return token(PRECISION); }
    "sampler1D"               { return token(SAMPLER1D); }
    "sampler2D"               { return token(SAMPLER2D); }
    "sampler3D"               { return token(SAMPLER3D); }
    "samplerCube"             { return token(SAMPLERCUBE); }
    "sampler1DShadow"         { return token(SAMPLER1DSHADOW); }
    "sampler2DShadow"         { return token(SAMPLER2DSHADOW); }
    "samplerCubeShadow"       { return token(SAMPLERCUBESHADOW); }
    "sampler1DArray"          { return token(SAMPLER1DARRAY); }
    "sampler2DArray"          { return token(SAMPLER2DARRAY); }
    "sampler1DArrayShadow"    { return token(SAMPLER1DARRAYSHADOW); }
    "sampler2DArrayShadow"    { return token(SAMPLER2DARRAYSHADOW); }
    "isampler1D"              { return token(ISAMPLER1D); }
    "isampler2D"              { return token(ISAMPLER2D); }
    "isampler3D"              { return token(ISAMPLER3D); }
    "isamplerCube"            { return token(ISAMPLERCUBE); }
    "isampler1DArray"         { return token(ISAMPLER1DARRAY); }
    "isampler2DArray"         { return token(ISAMPLER2DARRAY); }
    "usampler1D"              { return token(USAMPLER1D); }
    "usampler2D"              { return token(USAMPLER2D); }
    "usampler3D"              { return token(USAMPLER3D); }
    "usamplerCube"            { return token(USAMPLERCUBE); }
    "usampler1DArray"         { return token(USAMPLER1DARRAY); }
    "usampler2DArray"         { return token(USAMPLER2DARRAY); }
    "sampler2DRect"           { return token(SAMPLER2DRECT); }
    "sampler2DRectShadow"     { return token(SAMPLER2DRECTSHADOW); }
    "isampler2DRect"          { return token(ISAMPLER2DRECT); }
    "usampler2DRect"          { return token(USAMPLER2DRECT); }
    "samplerBuffer"           { return token(SAMPLERBUFFER); }
    "isamplerBuffer"          { return token(ISAMPLERBUFFER); }
    "usamplerBuffer"          { return token(USAMPLERBUFFER); }
    "sampler2DMS"             { return token(SAMPLER2DMS); }
    "isampler2DMS"            { return token(ISAMPLER2DMS); }
    "usampler2DMS"            { return token(USAMPLER2DMS); }
    "sampler2DMSArray"        { return token(SAMPLER2DMSARRAY); }
    "isampler2DMSArray"       { return token(ISAMPLER2DMSARRAY); }
    "usampler2DMSArray"       { return token(USAMPLER2DMSARRAY); }
    "samplerCubeArray"        { return token(SAMPLERCUBEARRAY); }
    "samplerCubeArrayShadow"  { return token(SAMPLERCUBEARRAYSHADOW); }
    "isamplerCubeArray"       { return token(ISAMPLERCUBEARRAY); }
    "usamplerCubeArray"       { return token(USAMPLERCUBEARRAY); }
    "image1D"                 { return token(IMAGE1D); }
    "iimage1D"                { return token(IIMAGE1D); }
    "uimage1D"                { return token(UIMAGE1D); }
    "image2D"                 { return token(IMAGE2D); }
    "iimage2D"                { return token(IIMAGE2D); }
    "uimage2D"                { return token(UIMAGE2D); }
    "image3D"                 { return token(IMAGE3D); }
    "iimage3D"                { return token(IIMAGE3D); }
    "uimage3D"                { return token(UIMAGE3D); }
    "image2DRect"             { return token(IMAGE2DRECT); }
    "iimage2DRect"            { return token(IIMAGE2DRECT); }
    "uimage2DRect"            { return token(UIMAGE2DRECT); }
    "imageCube"               { return token(IMAGECUBE); }
    "iimageCube"              { return token(IIMAGECUBE); }
    "uimageCube"              { return token(UIMAGECUBE); }
    "imageBuffer"             { return token(IMAGEBUFFER); }
    "iimageBuffer"            { return token(IIMAGEBUFFER); }
    "uimageBuffer"            { return token(UIMAGEBUFFER); }
    "image1DArray"            { return token(IMAGE1DARRAY); }
    "iimage1DArray"           { return token(IIMAGE1DARRAY); }
    "uimage1DArray"           { return token(UIMAGE1DARRAY); }
    "image2DArray"            { return token(IMAGE2DARRAY); }
    "iimage2DArray"           { return token(IIMAGE2DARRAY); }
    "uimage2DArray"           { return token(UIMAGE2DARRAY); }
    "imageCubeArray"          { return token(IMAGECUBEARRAY); }
    "iimageCubeArray"         { return token(IIMAGECUBEARRAY); }
    "uimageCubeArray"         { return token(UIMAGECUBEARRAY); }
    "image2DMS"               { return token(IMAGE2DMS); }
    "iimage2DMS"              { return token(IIMAGE2DMS); }
    "uimage2DMS"              { return token(UIMAGE2DMS); }
    "image2DMSArray"          { return token(IMAGE2DMSARRAY); }
    "iimage2DMSArray"         { return token(IIMAGE2DMSARRAY); }
    "uimage2DMSArray"         { return token(UIMAGE2DMSARRAY); }
    "struct"                  { return token(STRUCT); }
}

/* future keywords */
<YYINITIAL> {
    "common"                  { return token(COMMON); }
    "partition"               { return token(PARTITION); }
    "active"                  { return token(ACTIVE); }
    "asm"                     { return token(ASM); }
    "class"                   { return token(CLASS); }
    "union"                   { return token(UNION); }
    "enum"                    { return token(ENUM); }
    "typedef"                 { return token(TYPEDEF); }
    "template"                { return token(TEMPLATE); }
    "this"                    { return token(THIS); }
    "resource"                { return token(RESOURCE); }
    "goto"                    { return token(GOTO); }
    "inline"                  { return token(INLINE); }
    "noinline"                { return token(NOINLINE); }
    "public"                  { return token(PUBLIC); }
    "static"                  { return token(STATIC); }
    "extern"                  { return token(EXTERN); }
    "external"                { return token(EXTERNAL); }
    "interface"               { return token(INTERFACE); }
    "long"                    { return token(LONG); }
    "short"                   { return token(SHORT); }
    "half"                    { return token(HALF); }
    "fixed"                   { return token(FIXED); }
    "unsigned"                { return token(UNSIGNED); }
    "superp"                  { return token(SUPERP); }
    "input"                   { return token(INPUT); }
    "output"                  { return token(OUTPUT); }
    "hvec2"                   { return token(HVEC2); }
    "hvec3"                   { return token(HVEC3); }
    "hvec4"                   { return token(HVEC4); }
    "fvec2"                   { return token(FVEC2); }
    "fvec3"                   { return token(FVEC3); }
    "fvec4"                   { return token(FVEC4); }
    "sampler3DRect"           { return token(SAMPLER3DRECT); }
    "filter"                  { return token(FILTER); }
    "sizeof"                  { return token(SIZEOF); }
    "cast"                    { return token(CAST); }
    "namespace"               { return token(NAMESPACE); }
    "using"                   { return token(USING); }
}



<YYINITIAL> {
  /* identifiers */
  {Identifier}                   { return token(IDENTIFIER); }

  /* literals */
  {DecIntegerLiteral}            { return token(INTEGER_LITERAL); }
  \"                             { string.setLength(0); yybegin(STRING); }

  /* operators */
  "="                            { return token(OP_EQ); }
  "=="                           { return token(OP_EQEQ); }
  "+"                            { return token(OP_PLUS); }

  /* comments */
  {Comment}                      { /* ignore */ }

  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
  {PreProcessorStatement}        { /* ignore */ }
}

<STRING> {
  \"                             { yybegin(YYINITIAL); return token(STRING_LITERAL, string.toString()); }
  [^\n\r\"\\]+                   { string.append( yytext() ); }
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }

  \\r                            { string.append('\r'); }
  \\\"                           { string.append('\"'); }
  \\                             { string.append('\\'); }
}

/* error fallback */
[^]                              { throw new Error("Illegal character <"+ yytext()+">"); }
