package test.task.search.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import test.task.search.service.DocumentService;
import test.task.search.service.dto.DocumentDTO;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing Document.
 */
@RestController
@Slf4j
@RequestMapping("/api/documents")
public class DocumentResource {

    private final DocumentService documentService;

    public DocumentResource(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * POST  /documents : Create a new document.
     *
     * @param documentDTO the documentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new documentDTO, or with status 400 (Bad Request) if the document has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping()
    public ResponseEntity<DocumentDTO> createDocument(@RequestBody @Valid DocumentDTO documentDTO, BindingResult bindingResult) throws URISyntaxException {
        log.debug("REST request to save Document : {}", documentDTO);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        documentService.saveDocument(documentDTO);

        return ResponseEntity
                .created(new URI("/api/documents/" + documentDTO.getKey()))
                .build();
    }

    /**
     * GET  /documents/:key : get the "key" document.
     *
     * @param key the key of the documentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the documentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/{key}")
    public ResponseEntity<DocumentDTO> getDocument(@PathVariable String key) {
        log.debug("REST request to get Document : {}", key);

        return documentService.getByKey(key)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    /**
     * GET  /documents?:query : search documents by the provided content.
     *
     * @param query the content of the documents to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the list of matched documents
     */
    @GetMapping
    public ResponseEntity<List<DocumentDTO>> searchDocuments(@RequestParam String query) {
        log.debug("REST request to search Documents : {}", query);

        return ResponseEntity.ok(documentService.searchByTokens(query));
    }

}
