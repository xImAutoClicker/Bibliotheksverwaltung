package de.fhdw.project.library.util.map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.fhdw.project.library.exception.LibraryException;
import de.fhdw.project.library.util.response.ErrorType;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class ExchangeMap extends HashMap<String, Object> {

    private final static Gson gson = new GsonBuilder().create();

    private HttpStatus httpStatus;

    public ExchangeMap(final String key, final Object value){
        this.put(key, value);
    }

    @Override
    public ExchangeMap put(final String key, final Object value){
        super.put(key, value);
        return this;
    }

    public final ExchangeMap putMap(final Map<? extends String, ?> exchangeMap){
        super.putAll(exchangeMap);
        return this;
    }

    public final boolean containsKeys(final String... keys){
        for(String key : keys){
            if(!this.containsKey(key))
                return false;
        }
        return true;
    }

    public final boolean checkMap(final ExchangeDataType... dataTypes){
        for(ExchangeDataType dataType : dataTypes){
            final Object value = this.get(dataType.key());
            if(value == null || !value.getClass().equals(dataType.clazz()))return false;
        }
        return true;
    }

    public final int getInt(final String key){
        return (int)(double)this.get(key);
    }

    public final long getLong(final String key){
        return (long)(double)this.get(key);
    }

    public final float getFloat(final String key){
        return (float)(double)this.get(key);
    }

    public final double getDouble(final String key){
        return (double)this.get(key);
    }

    public final boolean getBoolean(final String key){
        return (boolean)this.get(key);
    }

    public final String getString(final String key){
        return (String)this.get(key);
    }

    public final <T> List<T> getList(final String key){
        return (List<T>)this.get(key);
    }

    public final <K,V> Map<K,V> getMap(final String key){
        return (Map<K,V>)this.get(key);
    }

    public final String toJson(){
        return gson.toJson(this);
    }

    public final ResponseEntity<String> toResponseEntity(){
        return this.toResponseEntity(this.httpStatus);
    }

    public final ResponseEntity<String> toResponseEntity(final HttpStatus httpStatus){
        return new ResponseEntity<>(this.toJson(), httpStatus);
    }

    public static ExchangeMap fromJson(final String json){
        try {
            return new ExchangeMap().putMap(gson.fromJson(json, HashMap.class));
        }catch (Exception e){
            return null;
        }
    }

    public static ExchangeMap fromJsonWithError(final String json) throws LibraryException {
        final ExchangeMap exchangeMap = fromJson(json);
        if(exchangeMap == null)
            throw new LibraryException(ErrorType.BAD_REQUEST);
        return exchangeMap;
    }
}
