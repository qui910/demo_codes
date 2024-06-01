package org.example.designPatterns.creationalPattern.factoryMethodPattern.demo1;

/**
 * 具体工厂类，使用了反射对具体产品的实例化
 * @version 1.0
 * @date 2023-08-10 14:34
 * @since 1.8
 **/
public class ConcreteFactory extends AbstractFactory {
    @Override
    public <T extends Product> T create(Class<T> cls) {
        Product product=null;
        try{
            product= (Product) Class.forName(cls.getName()).newInstance();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return (T) product;
    }
}
