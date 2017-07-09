/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TestbedTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TestArtifactDetailComponent } from '../../../../../../main/webapp/app/entities/test-artifact/test-artifact-detail.component';
import { TestArtifactService } from '../../../../../../main/webapp/app/entities/test-artifact/test-artifact.service';
import { TestArtifact } from '../../../../../../main/webapp/app/entities/test-artifact/test-artifact.model';

describe('Component Tests', () => {

    describe('TestArtifact Management Detail Component', () => {
        let comp: TestArtifactDetailComponent;
        let fixture: ComponentFixture<TestArtifactDetailComponent>;
        let service: TestArtifactService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TestbedTestModule],
                declarations: [TestArtifactDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TestArtifactService,
                    JhiEventManager
                ]
            }).overrideTemplate(TestArtifactDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TestArtifactDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TestArtifactService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TestArtifact(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.testArtifact).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
