import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { TestSuite } from './test-suite.model';
import { TestSuiteService } from './test-suite.service';

@Component({
    selector: 'jhi-test-suite-detail',
    templateUrl: './test-suite-detail.component.html'
})
export class TestSuiteDetailComponent implements OnInit, OnDestroy {

    testSuite: TestSuite;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private testSuiteService: TestSuiteService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTestSuites();
    }

    load(id) {
        this.testSuiteService.find(id).subscribe((testSuite) => {
            this.testSuite = testSuite;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTestSuites() {
        this.eventSubscriber = this.eventManager.subscribe(
            'testSuiteListModification',
            (response) => this.load(this.testSuite.id)
        );
    }
}
