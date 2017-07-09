import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TestArtifact } from './test-artifact.model';
import { TestArtifactPopupService } from './test-artifact-popup.service';
import { TestArtifactService } from './test-artifact.service';

@Component({
    selector: 'jhi-test-artifact-delete-dialog',
    templateUrl: './test-artifact-delete-dialog.component.html'
})
export class TestArtifactDeleteDialogComponent {

    testArtifact: TestArtifact;

    constructor(
        private testArtifactService: TestArtifactService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.testArtifactService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'testArtifactListModification',
                content: 'Deleted an testArtifact'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-test-artifact-delete-popup',
    template: ''
})
export class TestArtifactDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private testArtifactPopupService: TestArtifactPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.testArtifactPopupService
                .open(TestArtifactDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
