package sv.com.tienda.web.validators;

import org.primefaces.model.UploadedFile;
import org.primefaces.validate.ClientValidator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Map;

@FacesValidator(value = "FileNullValidator")
public class FileUploadCustomValidator implements Validator, ClientValidator {
    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        if(o instanceof UploadedFile){
            UploadedFile archivo = (UploadedFile) o;
            if(archivo.getSize() < 1){
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de validacion", "No has subido ninguna imagen"));
            }
        }
    }

    @Override
    public Map<String, Object> getMetadata() {
        return null;
    }

    @Override
    public String getValidatorId() {
        return "FileNullValidator";
    }
}
