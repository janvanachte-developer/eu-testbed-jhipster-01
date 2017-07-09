import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager , JhiDataUtils } from 'ng-jhipster';

import { TestArtifact } from './test-artifact.model';
import { TestArtifactService } from './test-artifact.service';

@Component({
    selector: 'jhi-test-artifact-detail',
    templateUrl: './test-artifact-detail.component.html'
})
export class TestArtifactDetailComponent implements OnInit, OnDestroy {

    testArtifact: TestArtifact;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private testArtifactService: TestArtifactService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTestArtifacts();
    }

    load(id) {
        this.testArtifactService.find(id).subscribe((testArtifact) => {
            this.testArtifact = testArtifact;
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTestArtifacts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'testArtifactListModification',
            (response) => this.load(this.testArtifact.id)
        );
    }
}
