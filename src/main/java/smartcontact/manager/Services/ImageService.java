 package smartcontact.manager.Services;

import java.util.UUID;

import javax.print.DocFlavor.BYTE_ARRAY;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;

@Service
public class ImageService { 


    private Cloudinary cloudinary;

    public ImageService(Cloudinary cloudinary)
    {

        this.cloudinary=cloudinary;
    }


    //The working of this method is to save the file and return its url to access it

    public String uploadimg(MultipartFile image) {

        System.out.println("Saving image to cloudinary..."+image.getOriginalFilename());

        String filename=UUID.randomUUID().toString();

        try {
            
            byte[] data=new byte[image.getInputStream().available()];
            image.getInputStream().read(data);
            cloudinary.uploader().upload(data,ObjectUtils.asMap(
                "public_id",filename
            ));

        } catch (Exception e) {
            // TODO: handle exception
        }

        return this.getUrlFromPublic(filename);
            
        
            }
        

      //return a url through which we can excess the image and save it into database for further operations on image
            

            public String getUrlFromPublic(String publicid) {

                return cloudinary
                       .url()
                       .transformation(
                        new Transformation<>()
                        .width(500)
                        .height(500)
                        .crop("fill")
                       )
                       .generate(publicid);
            }

    
}