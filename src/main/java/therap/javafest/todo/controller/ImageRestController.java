package therap.javafest.todo.controller;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.DetectTextRequest;
import com.amazonaws.services.rekognition.model.DetectTextResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.TextDetection;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mahfuz.ahmed
 * @since 4/29/21
 */
@RestController
@RequestMapping("/api/v1/image")
public class ImageRestController {

    @PostMapping
    public String rekognize(@RequestBody MultipartFile file) throws IOException {
        if (file == null) {
            return null;
        }

        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();
        Image sourceImage =
                new Image()
                        .withBytes(ByteBuffer.wrap(
                                file.getBytes()));

        DetectTextRequest request = new DetectTextRequest()
                .withImage(sourceImage);

        DetectTextResult result = rekognitionClient.detectText(request);

        String text = "";
        List<TextDetection> filterdResult = result.getTextDetections()
                .stream()
                .filter(detection -> detection.getParentId() == null)
                .collect(Collectors.toList());

        for (TextDetection detection : filterdResult) {
            text += detection.getDetectedText() + " ";
        }

        return text;
    }
}
