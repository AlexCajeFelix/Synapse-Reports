package com.notifications.service.notification_service.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Controller responsável por servir os arquivos de relatório para download.
 * Ele expõe um endpoint GET para que os clientes possam baixar os relatórios
 * gerados.
 */
@RestController
@RequestMapping("/api/download") // Define o caminho base para este controller
public class DownloadController {

    @Value("${reports.storage.path}")
    private String storagePath;

    /**
     * Endpoint para baixar um arquivo de relatório específico.
     * O nome do arquivo é passado como uma variável de caminho na URL.
     *
     * Exemplo de uso: GET
     * http://localhost:8084/api/download/relatorio_1678888888.pdf
     *
     * @param fileName O nome completo do arquivo a ser baixado (incluindo
     *                 extensão).
     * @param request  A requisição HTTP, usada para determinar o tipo de conteúdo
     *                 do arquivo.
     * @return ResponseEntity contendo o arquivo como um recurso, junto com os
     *         cabeçalhos HTTP apropriados.
     */
    @GetMapping("/{fileName:.+}") // {fileName:.+} permite que o nome do arquivo contenha pontos (ex: .pdf, .txt)
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        try {

            Path filePath = Paths.get(storagePath).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            // Verifica se o arquivo existe e é acessível
            if (resource.exists() && resource.isReadable()) {
                // Tenta determinar o tipo de conteúdo do arquivo (ex: application/pdf,
                // text/plain)
                String contentType = null;
                try {
                    contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
                } catch (IOException ex) {
                    System.out.println(
                            "Não foi possível determinar o tipo de arquivo para " + fileName + ". Usando padrão.");
                }

                // Se o tipo de conteúdo não pôde ser determinado, define um padrão
                if (contentType == null) {
                    contentType = "application/octet-stream"; // Tipo genérico para download de binários
                }

                // Retorna o arquivo como um recurso no corpo da resposta HTTP,
                // junto com os cabeçalhos para download.
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {

                // Se o arquivo não existe ou não pode ser lido, retorna 404 Not Found
                System.out.println("Arquivo não encontrado ou inacessível: " + fileName);
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException ex) {
            // Lida com URLs malformadas ou problemas ao criar o recurso de URL
            System.err
                    .println("Erro: URL malformada ao tentar carregar o arquivo " + fileName + ": " + ex.getMessage());
            return ResponseEntity.badRequest().build(); // Retorna 400 Bad Request
        } catch (Exception ex) {
            // Captura outras exceções inesperadas
            System.err.println("Erro inesperado ao tentar baixar o arquivo " + fileName + ": " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.internalServerError().build(); // Retorna 500 Internal Server Error
        }
    }

}
