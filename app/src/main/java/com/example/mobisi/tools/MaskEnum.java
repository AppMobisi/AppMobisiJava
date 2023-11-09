package com.example.mobisi.tools;

public enum MaskEnum {
    CPF("###.###.###-##"),
    TELEFONE("(##) #####-####"),
    NUMERO_CARTAO("#### #### #### ####"),
    CEP("#####-###"),
    DATA("##/##");
    private String mask;
    MaskEnum(String mask){
        this.mask = mask;
    }
    public String getMask(){
        return this.mask;
    }
}
